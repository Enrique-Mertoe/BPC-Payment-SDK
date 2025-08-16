package org.abutimartin.bpcpayment.service;

import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.http.HttpClient;
import org.abutimartin.bpcpayment.model.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

public class BindingService {
    private final HttpClient httpClient;
    private final BomaPayConfig config;
    
    public BindingService(HttpClient httpClient, BomaPayConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
    
    public BaseResponse getBindings(String clientId) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("clientId", clientId);
        
        return httpClient.postForm("/rest/getBindings.do", formData, BaseResponse.class);
    }
    
    public BaseResponse getBindingsByCardOrId(String pan) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("pan", pan);
        
        return httpClient.postForm("/rest/getBindingsByCardOrId.do", formData, BaseResponse.class);
    }
    
    public BaseResponse unBindCard(String bindingId) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("bindingId", bindingId);
        
        return httpClient.postForm("/rest/unBindCard.do", formData, BaseResponse.class);
    }
    
    public BaseResponse bindCard(String bindingId) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("bindingId", bindingId);
        
        return httpClient.postForm("/rest/bindCard.do", formData, BaseResponse.class);
    }
    
    public BaseResponse extendBinding(String bindingId, String newExpiry) throws BomaPayException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", config.getUsername());
        formData.put("password", config.getPassword());
        formData.put("bindingId", bindingId);
        formData.put("newExpiry", newExpiry);
        formData.put("language", config.getLanguage());
        
        return httpClient.postForm("/rest/extendBinding.do", formData, BaseResponse.class);
    }
}