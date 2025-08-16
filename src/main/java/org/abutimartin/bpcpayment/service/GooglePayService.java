package org.abutimartin.bpcpayment.service;

import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.http.HttpClient;

public class GooglePayService {
    private final HttpClient httpClient;
    private final BomaPayConfig config;
    
    public GooglePayService(HttpClient httpClient, BomaPayConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
}