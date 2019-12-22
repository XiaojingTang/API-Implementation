package springboot.service;

import springboot.model.AccountRepository;

@org.springframework.stereotype.Component
public class InputValidator {
    public boolean isUserValid(String user) {
        return user != null && user.length() != 0;
    }

    public boolean isCurrencyValid(String currency, ConversionRateAdaptor conversionRateAdaptor) {
        return currency != null && currency.length() != 0 && conversionRateAdaptor.isValidCurrency(currency);
    }

    public boolean isAccountIdValid(long accountId, AccountRepository accountRepository) {
        return accountRepository.existsById(accountId);
    }

    public boolean isAmountValid(double amount) {
        return amount > 0.0;
    }

    public boolean isRateIssuedDate(String date, ConversionRateAdaptor conversionRateAdaptor) {
        return conversionRateAdaptor.isRateIssued(date);
    }
}
