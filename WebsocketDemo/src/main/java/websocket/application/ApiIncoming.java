package websocket.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.messages.AllAccountsMsg;
import websocket.messages.AllFundRecordsMsg;
import websocket.messages.AllTransferRecordsMsg;
import websocket.messages.NotificationMsg;
import websocket.objects.Account;
import websocket.objects.FundRecord;
import websocket.objects.TransferRecord;
import websocket.persistence.Persister;

import java.util.List;

public class ApiIncoming implements EngineIncoming {
    private static Logger logger = LogManager.getLogger(ApiIncoming.class);
    //    private ConversionRateAdaptor conversionRateAdaptor = new ConversionRateAdaptor();
    private Persister persister;
    private EngineOutgoing outgoing;

    public ApiIncoming(Persister persister) {
        this.persister = persister;
    }

    @Override
    public void setOutgoing(EngineOutgoing outgoing) {
        this.outgoing = outgoing;
    }

    @Override
    public void createAccount(String user, String currency) {
        persister.addToAccount(user, currency);
        outgoing.sendNotification(new NotificationMsg("Accounted created."));
    }

    @Override
    public void fundAccount(long accountId, String transCurrency, double amount, String date) {
//        if (!conversionRateAdaptor.isRateIssued(date)) {
//            outgoing.sendNotification(new NotificationMsg("No conversion rate available on the date."));
//            return;
//        }
//        if (!conversionRateAdaptor.isValidCurrency(transCurrency)) {
//            outgoing.sendNotification(new NotificationMsg("Currency is not valid."));
//            return;
//        }

        Account account = persister.findById(accountId);
        if (account == null) {
            outgoing.sendNotification(new NotificationMsg("accountId is not valid."));
            return;
        }
//        ConversionRate conversionRate = conversionRateAdaptor.getConversionRate(date, transCurrency, account.getCurrency(), String.valueOf(amount));
//        persister.setAccountBalance(accountId, account.getBalance() + (double) conversionRate.get("data.crdhldBillAmt"));
//        persister.addToFundRecord(accountId, transCurrency, amount, date, (double) conversionRate.get("data.conversionRate"),
//                (double) conversionRate.get("data.crdhldBillAmt"), (long) conversionRate.get("data.bankFee"));
//        outgoing.sendNotification(new NotificationMsg("Accounted funded."));
    }

    @Override
    public void transferFund(long fromAccountId, long toAccountId, String transCurrency, double amount, String date) {
//        if (!conversionRateAdaptor.isRateIssued(date)) {
//            outgoing.sendNotification(new NotificationMsg("No conversion rate available on the date."));
//            return;
//        }
//        if (!conversionRateAdaptor.isValidCurrency(transCurrency)) {
//            outgoing.sendNotification(new NotificationMsg("Currency is not valid."));
//            return;
//        }

        Account fromAccount = persister.findById(fromAccountId);
        if (fromAccount == null) {
            outgoing.sendNotification(new NotificationMsg("fromAccountId is not valid."));
            return;
        }
        if (fromAccount.getBalance() < amount) {
            outgoing.sendNotification(new NotificationMsg("fromAccount does not have enough balance."));
            return;
        }
        Account toAccount = persister.findById(toAccountId);
        if (toAccount == null) {
            outgoing.sendNotification(new NotificationMsg("toAccountId is not valid."));
            return;
        }
//        ConversionRate conversionRate = conversionRateAdaptor.getConversionRate(date, transCurrency, toAccount.getCurrency(), String.valueOf(amount));
//        persister.setAccountBalance(fromAccountId, fromAccount.getBalance() - amount);
//        persister.setAccountBalance(toAccountId, toAccount.getBalance() + (double) conversionRate.get("data.crdhldBillAmt"));
//        persister.addToTransferRecord(fromAccountId, toAccountId, transCurrency, amount, date, (double) conversionRate.get("data.conversionRate"),
//                (double) conversionRate.get("data.crdhldBillAmt"), (long) conversionRate.get("data.bankFee"));
//        outgoing.sendNotification(new NotificationMsg("Fund transferred."));
    }

    @Override
    public void listAllAccounts() {
        List<Account> accounts = persister.getAllAccounts();
        outgoing.sendAllAccounts(new AllAccountsMsg(accounts));
    }

    @Override
    public void listAllFundRecords() {
        List<FundRecord> fundRecords = persister.getAllFundRecord();
        outgoing.sendAllFundRecords(new AllFundRecordsMsg(fundRecords));
    }

    @Override
    public void listAllTransferRecords() {
        List<TransferRecord> transferRecords = persister.getAllTransferRecord();
        outgoing.sendAllTransferRecords(new AllTransferRecordsMsg(transferRecords));
    }
}
