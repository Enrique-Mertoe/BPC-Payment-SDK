package org.abutimartin.bpcpayment.service;

import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.http.HttpClient;
import org.abutimartin.bpcpayment.model.request.OrderRegistrationRequest;
import org.abutimartin.bpcpayment.model.response.OrderRegistrationResponse;
import org.abutimartin.bpcpayment.model.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

public class OrderService {
    private final HttpClient httpClient;
    private final BomaPayConfig config;
    
    public OrderService(HttpClient httpClient, BomaPayConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
    
    public OrderRegistrationResponse register(Long amount, String orderNumber, String returnUrl) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("amount", amount.toString());
        formData.put("currency", config.getCurrency());
        formData.put("language", config.getLanguage());
        formData.put("orderNumber", orderNumber);
        formData.put("returnUrl", returnUrl);
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        if (config.getClientId() != null) {
            formData.put("clientId", config.getClientId());
        }
        
        return httpClient.postForm("/rest/register.do", formData, OrderRegistrationResponse.class);
    }
    
    public OrderRegistrationResponse registerPreAuth(Long amount, String orderNumber, String returnUrl) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("amount", amount.toString());
        formData.put("currency", config.getCurrency());
        formData.put("language", config.getLanguage());
        formData.put("orderNumber", orderNumber);
        formData.put("returnUrl", returnUrl);
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        
        return httpClient.postForm("/rest/registerPreAuth.do", formData, OrderRegistrationResponse.class);
    }
    
    public BaseResponse deposit(String orderId, Long amount) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("amount", amount.toString());
        formData.put("orderId", orderId);
        formData.put("language", config.getLanguage());
        
        return httpClient.postForm("/rest/deposit.do", formData, BaseResponse.class);
    }
    
    public BaseResponse reverse(String orderId) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("orderId", orderId);
        formData.put("language", config.getLanguage());
        
        return httpClient.postForm("/rest/reverse.do", formData, BaseResponse.class);
    }
    
    public BaseResponse refund(String orderId, Long amount) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("amount", amount.toString());
        formData.put("orderId", orderId);
        formData.put("language", config.getLanguage());
        
        return httpClient.postForm("/rest/refund.do", formData, BaseResponse.class);
    }
    
    public BaseResponse decline(String orderId, String orderNumber) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("orderId", orderId);
        formData.put("orderNumber", orderNumber);
        formData.put("merchantLogin", config.getMerchantLogin());
        formData.put("language", config.getLanguage());
        
        return httpClient.postForm("/rest/decline.do", formData, BaseResponse.class);
    }
}