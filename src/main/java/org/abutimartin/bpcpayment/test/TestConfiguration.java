package org.abutimartin.bpcpayment.test;

import org.abutimartin.bpcpayment.BomaPayClient;
import org.abutimartin.bpcpayment.config.BomaPayConfig;

public class TestConfiguration {
    
    public static final String SANDBOX_BASE_URL = "https://dev.bpcbt.com/payment";
    public static final String TEST_USERNAME = "test_user";
    public static final String TEST_PASSWORD = "test_user_password";
    public static final String TEST_CLIENT_ID = "259753456";
    public static final String TEST_MERCHANT_LOGIN = "OurBestMerchantLogin";
    public static final String TEST_RETURN_URL = "https://mybestmerchantreturnurl.com";
    public static final String DEFAULT_CURRENCY = "978"; // EUR
    public static final String DEFAULT_LANGUAGE = "en";
    
    public static BomaPayClient createTestClient() {
        BomaPayConfig config = BomaPayConfig.builder()
                .baseUrl(SANDBOX_BASE_URL)
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .clientId(TEST_CLIENT_ID)
                .merchantLogin(TEST_MERCHANT_LOGIN)
                .currency(DEFAULT_CURRENCY)
                .language(DEFAULT_LANGUAGE)
                .timeout(30000)
                .build();
        
        return new BomaPayClient(config);
    }
    
    public static String generateTestOrderNumber() {
        return "TEST-ORDER-" + System.currentTimeMillis();
    }
    
    public static String generateTestOrderNumber(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }
    
    public static Long getTestAmount() {
        return 10000L; // 100.00 in minor units
    }
    
    public static Long getTestAmount(double amount) {
        return Math.round(amount * 100);
    }
}