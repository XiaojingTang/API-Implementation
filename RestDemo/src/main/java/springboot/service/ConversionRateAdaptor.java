package springboot.service;
/**
 * Script-Name: example_conversion_rate_request
 */

import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.Environment;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.core.model.map.SmartMap;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.currencyconversion.ConversionRate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

@org.springframework.stereotype.Component
public class ConversionRateAdaptor {
    private String consumerKey = "B8BXFFro16_MkeAk5zUAUKu8h-yUz87sqegKP8z-291d2e28!4c60a7d76d0b4814b183635d2bdf21890000000000000000";   // You should copy this from "My Keys" on your project page e.g. UTfbhDCSeNYvJpLL5l028sWL9it739PYh6LU5lZja15xcRpY!fd209e6c579dc9d7be52da93d35ae6b6c167c174690b72fa
    private String keyAlias = "keyalias";   // For production: change this to the key alias you chose when you created your production key
    private String keyPassword = "keystorepassword";   // For production: change this to the key alias you chose when you created your production key
    private InputStream is; // e.g. /Users/yourname/project/sandbox.p12 | C:\Users\yourname\project\sandbox.p12

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
    }

    public ConversionRate getConversionRate(RequestMap map) {
        try {
            return ConversionRate.query(map);
        } catch (ApiException e) {
            err("HttpStatus: " + e.getHttpStatus());
            err("Message: " + e.getMessage());
            err("ReasonCode: " + e.getReasonCode());
            err("Source: " + e.getSource());
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        ConversionRateAdaptor conversionRateAdaptor = new ConversionRateAdaptor();
        RequestMap map = new RequestMap();
        map.set("fxDate", "2019-09-30");
        map.set("transCurr", "ALL");
        map.set("crdhldBillCurr", "DZD");
        map.set("bankFee", "5");
        map.set("transAmt", "23");

        ConversionRate response = conversionRateAdaptor.getConversionRate(map);
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