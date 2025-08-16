package org.abutimartin.bpcpayment.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType FORM_ENCODED = MediaType.get("application/x-www-form-urlencoded");
    
    private final OkHttpClient client;
    private final BomaPayConfig config;
    private final ObjectMapper objectMapper;
    
    public HttpClient(BomaPayConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
    
    public <T> T postJson(String endpoint, Object request, Class<T> responseType) throws BomaPayException {
        try {
            String json = objectMapper.writeValueAsString(request);
            RequestBody body = RequestBody.create(json, JSON);
            
            Request httpRequest = new Request.Builder()
                    .url(config.getBaseUrl() + endpoint)
                    .post(body)
                    .build();
            
            return executeRequest(httpRequest, responseType);
        } catch (Exception e) {
            logger.error("Error making JSON POST request to {}", endpoint, e);
            throw new BomaPayException("Failed to make request", e);
        }
    }
    
    public <T> T postForm(String endpoint, Map<String, String> formData, Class<T> responseType) throws BomaPayException {
        try {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                if (entry.getValue() != null) {
                    formBuilder.add(entry.getKey(), entry.getValue());
                }
            }
            
            Request httpRequest = new Request.Builder()
                    .url(config.getBaseUrl() + endpoint)
                    .post(formBuilder.build())
                    .build();
            
            return executeRequest(httpRequest, responseType);
        } catch (Exception e) {
            logger.error("Error making form POST request to {}", endpoint, e);
            throw new BomaPayException("Failed to make request", e);
        }
    }
    
    private <T> T executeRequest(Request request, Class<T> responseType) throws IOException, BomaPayException {
        logger.debug("Making request to: {}", request.url());
        
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            
            if (!response.isSuccessful()) {
                logger.error("HTTP error {}: {}", response.code(), responseBody);
                throw new BomaPayException("HTTP error: " + response.code() + " " + response.message());
            }
            
            logger.debug("Response: {}", responseBody);
            return objectMapper.readValue(responseBody, responseType);
        }
    }
}