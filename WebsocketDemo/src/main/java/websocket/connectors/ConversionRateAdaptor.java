package websocket.connectors;
/**
 * Script-Name: example_conversion_rate_request
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.Environment;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.core.model.map.SmartMap;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.currencyconversion.ConversionRate;
import com.mastercard.api.currencyconversion.Currencies;
import com.mastercard.api.currencyconversion.RateIssued;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConversionRateAdaptor {
    private String consumerKey = "B8BXFFro16_MkeAk5zUAUKu8h-yUz87sqegKP8z-291d2e28!4c60a7d76d0b4814b183635d2bdf21890000000000000000";   // You should copy this from "My Keys" on your project page e.g. UTfbhDCSeNYvJpLL5l028sWL9it739PYh6LU5lZja15xcRpY!fd209e6c579dc9d7be52da93d35ae6b6c167c174690b72fa
    private String keyAlias = "keyalias";   // For production: change this to the key alias you chose when you created your production key
    private String keyPassword = "keystorepassword";   // For production: change this to the key alias you chose when you created your production key
    private InputStream is; // e.g. /Users/yourname/project/sandbox.p12 | C:\Users\yourname\project\sandbox.p12
    private static final String BANKFEE = "5";
    private Set<String> allCurrencies = new HashSet<>();
    private static Logger logger = LogManager.getLogger(ConversionRateAdaptor.class);
    private Gson gson = new GsonBuilder().create();

    public ConversionRateAdaptor() {
        try {
            is = new FileInputStream("C:\\Users\\Jean\\Mastercard\\MCD_Sandbox_APIDemo_API_Keys\\APIDemo-sandbox.p12");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ApiConfig.setAuthentication(new OAuthAuthentication(consumerKey, is, keyAlias, keyPassword));   // You only need to set this once
        ApiConfig.setDebug(true);   // Enable http wire logging
        // This is needed to change the environment to run the sample code. For production: use ApiConfig.setSandbox(false);
        ApiConfig.setEnvironment(Environment.parse("sandbox_mtf"));
        allCurrencies = getAllCurrencies();
    }

    private Set<String> getAllCurrencies() {
        Set<String> currencies = new HashSet<>();
        try {
            RequestMap map = new RequestMap();

            Currencies response = Currencies.query(map);
            logger.debug("getAllCurrencies response: " + gson.toJson(response, response.getClass()));
            for (Map<String, Object> item : (List<Map<String, Object>>) response.get("data.currencies")) {
                currencies.add((String) item.get("alphaCd"));
            }
        } catch (ApiException e) {
            logger.error("getAllCurrencies: ", e);
        }
        return currencies;
    }

    public boolean isValidCurrency(String currency) {
        return allCurrencies.contains(currency);
    }

    public boolean isRateIssued(String date) {
        RequestMap dateMap = new RequestMap();
        dateMap.set("date", date);

        try {
            RateIssued response = RateIssued.query(dateMap);
            logger.debug("RateIssued response: " + gson.toJson(response, response.getClass()));
            return (response.get("data.rateIssued") != null && response.get("data.rateIssued").equals("Yes"));
        } catch (ApiException e) {
            logger.error("isRateIssued: ", e);
        }
        return false;
    }

    public ConversionRate getConversionRate(String date, String transCurr, String crdhldBillCurr, String amount) {

        RequestMap map = new RequestMap();
        map.set("fxDate", date);
        map.set("transCurr", transCurr);
        map.set("crdhldBillCurr", crdhldBillCurr);
        map.set("bankFee", BANKFEE);
        map.set("transAmt", amount);

        try {
            return ConversionRate.query(map);
        } catch (ApiException e) {
            logger.error("getConversionRate: ", e);
        }
        return null;
    }

    public static void main(String[] args) {

        ConversionRateAdaptor conversionRateAdaptor = new ConversionRateAdaptor();

        System.out.println("isRateIssued return: " + conversionRateAdaptor.isRateIssued("1019-08-08"));

        System.out.println("The currencies set:");
        for (String s : conversionRateAdaptor.getAllCurrencies()) {
            System.out.println(s);
        }

        ConversionRate response = conversionRateAdaptor.getConversionRate("2019-09-30", "ALL", "DZD", "23");
        out(response, "name"); //-->settlement-conversion-rate
        out(response, "description"); //-->Settlement conversion rate and billing amount
        out(response, "date"); //-->2017-11-03 03:59:50
        out(response, "data.conversionRate"); //-->0.57
        out(response, "data.crdhldBillAmt"); //-->13.11
        out(response, "data.fxDate"); //-->2019-09-30
        out(response, "data.transCurr"); //-->ALL
        out(response, "data.crdhldBillCurr"); //-->DZD
        out(response, "data.transAmt"); //-->23
        out(response, "data.bankFee"); //-->5
    }

    public static void out(SmartMap response, String key) {
        System.out.println(key + "-->" + response.get(key));
    }

    public static void out(Map<String, Object> map, String key) {
        System.out.println(key + "--->" + map.get(key));
    }

    public static void err(String message) {
        System.err.println(message);
    }
}