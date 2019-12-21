package springboot.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mastercard.api.currencyconversion.ConversionRate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.model.*;
import springboot.service.ConversionRateAdaptor;

import java.util.List;

@RestController
@RequestMapping("/")
public class Controller {
    private Gson gson = new GsonBuilder().create();

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FundRecordRepository fundRecordRepository;

    @Autowired
    private TransferRecordRepository transferRecordRepository;

    @Autowired
    private ConversionRateAdaptor conversionRateAdaptor;

    @ApiOperation(value = "Get all accounts")
    @RequestMapping(value = "/getAllAccounts", method = RequestMethod.GET)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @ApiOperation(value = "Get all fund records")
    @RequestMapping(value = "/getAllFundRecords", method = RequestMethod.GET)
    public List<FundRecord> getAllFundRecords() {
        return fundRecordRepository.findAll();
    }

    @ApiOperation(value = "Get all transfer records")
    @RequestMapping(value = "/getAllTransferRecords", method = RequestMethod.GET)
    public List<TransferRecord> getAllTransferRecords() {
        return transferRecordRepository.findAll();
    }

    @Modifying
    @Transactional
    @ApiOperation(value = "Create an account")
    @RequestMapping(path = "/createAccount", method = RequestMethod.POST)
    public ResponseEntity<?> createAccount(
            @RequestParam String user,
            @RequestParam String currency) {

        if (user == null || user.length() == 0 || currency == null || currency.length() == 0) {
            return new ResponseEntity<>("User/Currency cannot be empty", HttpStatus.BAD_REQUEST);
        }

        // TODO: check duplicates?
        Account account = accountRepository.saveAndFlush(new Account(null, user, currency));
        Gson gson = new GsonBuilder().create();
        return new ResponseEntity<>(gson.toJson(account, account.getClass()), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @ApiOperation(value = "Fund an account")
    @RequestMapping(path = "/fundAccount", method = RequestMethod.POST)
    public ResponseEntity<?> fundAccount(
            @RequestParam long accountId,
            @RequestParam String transCurrency,
            @RequestParam double amount,
            @RequestParam String date) {

        if (accountId <= 0 || transCurrency == null || transCurrency.length() == 0) {
            return new ResponseEntity<>("AccountId must be positive / Currency cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Account account = accountRepository.findById(accountId);
        ConversionRate response = conversionRateAdaptor.getConversionRate(date, transCurrency, account.getCurrency(), Double.toString(amount));
        if (response == null || response.get("data.conversionRate") == null) {
            return new ResponseEntity<>("Currency conversion not available", HttpStatus.BAD_REQUEST);
        }
        double conversionRate = (double) response.get("data.conversionRate");
        double crdhldBillAmt = (double) response.get("data.crdhldBillAmt");
        long bankFee = (long) response.get("data.bankFee");
        account.setBalance(account.getBalance() + crdhldBillAmt - bankFee);
        accountRepository.saveAndFlush(account);      //todo synchonize?
        FundRecord fundRecord = fundRecordRepository.saveAndFlush(
                new FundRecord(null, accountId, transCurrency, amount, date, conversionRate, crdhldBillAmt, bankFee));
        return new ResponseEntity<>(gson.toJson(fundRecord, fundRecord.getClass()), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @ApiOperation(value = "Transfer funds between two accounts")
    @RequestMapping(path = "/transferFund", method = RequestMethod.POST)
    public ResponseEntity<?> transferFund(
            @RequestParam long fromAccountId,
            @RequestParam long toAccountId,
            @RequestParam String currency,
            @RequestParam double amount,
            @RequestParam String date) {

        if (fromAccountId <= 0 || toAccountId <= 0) {
            return new ResponseEntity<>("AccountId cannot be negative", HttpStatus.BAD_REQUEST);
        }
        if (currency == null || currency.length() == 0) {
            return new ResponseEntity<>("Currency cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Account fromAccount = accountRepository.findById(fromAccountId);
        if (amount > fromAccount.getBalance()) {
            return new ResponseEntity<>("FromAccount does not have enough balance", HttpStatus.BAD_REQUEST);
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        Account toAccount = accountRepository.findById(toAccountId);
        ConversionRate response = conversionRateAdaptor.getConversionRate(date, currency, toAccount.getCurrency(), Double.toString(amount));
        if (response == null) {
            return new ResponseEntity<>("Currency conversion not available", HttpStatus.BAD_REQUEST);
        }
        double conversionRate = (double) response.get("data.conversionRate");
        double crdhldBillAmt = (double) response.get("data.crdhldBillAmt");
        double bankFee = (double) response.get("data.bankFee");
        toAccount.setBalance(toAccount.getBalance() + crdhldBillAmt - bankFee);
        accountRepository.saveAndFlush(fromAccount);
        accountRepository.saveAndFlush(toAccount);

        TransferRecord transferRecord = transferRecordRepository.saveAndFlush(
                new TransferRecord(null, fromAccountId, toAccountId, currency, amount, date, conversionRate, crdhldBillAmt, bankFee));
        return new ResponseEntity<>(gson.toJson(transferRecord, transferRecord.getClass()), HttpStatus.OK);
    }
}