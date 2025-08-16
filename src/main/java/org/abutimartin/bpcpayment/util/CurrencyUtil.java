package org.abutimartin.bpcpayment.util;

import java.util.HashMap;
import java.util.Map;

public class CurrencyUtil {
    private static final Map<String, String> CURRENCY_CODES = new HashMap<>();
    
    static {
        CURRENCY_CODES.put("USD", "840");
        CURRENCY_CODES.put("EUR", "978");
        CURRENCY_CODES.put("GBP", "826");
        CURRENCY_CODES.put("RUB", "643");
        CURRENCY_CODES.put("KZT", "398");
        CURRENCY_CODES.put("UZS", "860");
        CURRENCY_CODES.put("MWK", "454");
    }
    
    public static String getCurrencyCode(String currency) {
        return CURRENCY_CODES.get(currency.toUpperCase());
    }
    
    public static boolean isValidCurrency(String currency) {
        return CURRENCY_CODES.containsKey(currency.toUpperCase());
    }
    
    public static Long convertToMinorUnits(Double amount, String currency) {
        return Math.round(amount * 100);
    }
    
    public static Double convertFromMinorUnits(Long amount, String currency) {
        return amount / 100.0;
    }
}