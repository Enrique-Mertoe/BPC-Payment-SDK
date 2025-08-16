package org.abutimartin.bpcpayment.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern PAN_PATTERN = Pattern.compile("^[0-9]{13,19}$");
    private static final Pattern CVC_PATTERN = Pattern.compile("^[0-9]{3,4}$");
    private static final Pattern EXPIRY_PATTERN = Pattern.compile("^[0-9]{6}$");
    private static final Pattern ORDER_NUMBER_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1,50}$");
    
    public static boolean isValidPan(String pan) {
        return pan != null && PAN_PATTERN.matcher(pan).matches();
    }
    
    public static boolean isValidCvc(String cvc) {
        return cvc != null && CVC_PATTERN.matcher(cvc).matches();
    }
    
    public static boolean isValidExpiry(String expiry) {
        if (expiry == null || !EXPIRY_PATTERN.matcher(expiry).matches()) {
            return false;
        }
        
        int year = Integer.parseInt(expiry.substring(0, 4));
        int month = Integer.parseInt(expiry.substring(4, 6));
        
        return year >= 2000 && year <= 2099 && month >= 1 && month <= 12;
    }
    
    public static boolean isValidOrderNumber(String orderNumber) {
        return orderNumber != null && ORDER_NUMBER_PATTERN.matcher(orderNumber).matches();
    }
    
    public static boolean isValidAmount(Long amount) {
        return amount != null && amount > 0;
    }
    
    public static boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }
}