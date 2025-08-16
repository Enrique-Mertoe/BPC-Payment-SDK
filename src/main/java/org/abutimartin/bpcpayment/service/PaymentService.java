package org.abutimartin.bpcpayment.service;

import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.http.HttpClient;
import org.abutimartin.bpcpayment.model.request.InstantPaymentRequest;
import org.abutimartin.bpcpayment.model.response.PaymentResponse;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    private final HttpClient httpClient;
    private final BomaPayConfig config;
    
    public PaymentService(HttpClient httpClient, BomaPayConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
    
    public PaymentResponse paymentOrder(String mdOrder, String pan, String cvc, String year, String month, String cardholderName) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("language", config.getLanguage());
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("MDORDER", mdOrder);
        formData.put("$PAN", pan);
        formData.put("$CVC", cvc);
        formData.put("YYYY", year);
        formData.put("MM", month);
        formData.put("TEXT", cardholderName);
        
        return httpClient.postForm("/rest/paymentorder.do", formData, PaymentResponse.class);
    }
    
    public PaymentResponse instantPayment(Long amount, String orderNumber, String description, 
                                        String pan, String cvc, String expiry, String cardholderName,
                                        String backUrl, String failUrl) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("amount", amount.toString());
        formData.put("currency", config.getCurrency());
        formData.put("description", description);
        formData.put("orderNumber", orderNumber);
        formData.put("pan", pan);
        formData.put("cvc", cvc);
        formData.put("expiry", expiry);
        formData.put("cardHolderName", cardholderName);
        formData.put("language", config.getLanguage());
        formData.put("backUrl", backUrl);
        formData.put("failUrl", failUrl);
        
        // Add common optional parameters that might be required
        if (config.getClientId() != null) {
            formData.put("clientId", config.getClientId());
        }
        formData.put("ip", "127.0.0.1"); // Default IP for testing
        
        return httpClient.postForm("/rest/instantPayment.do", formData, PaymentResponse.class);
    }
    
    public PaymentResponse motoPayment(Long amount, String description, String pan, String expiry,
                                     String cvc, String cardholder, String returnUrl) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("amount", amount.toString());
        formData.put("currency", config.getCurrency());
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("returnUrl", returnUrl);
        formData.put("description", description);
        formData.put("pan", pan);
        formData.put("expiry", expiry);
        formData.put("cvc", cvc);
        formData.put("cardholder", cardholder);
        formData.put("language", config.getLanguage());
        
        return httpClient.postForm("/rest/motoPayment.do", formData, PaymentResponse.class);
    }
}