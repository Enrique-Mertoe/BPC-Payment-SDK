package org.abutimartin.bpcpayment.service;

import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.http.HttpClient;

public class P2PService {
    private final HttpClient httpClient;
    private final BomaPayConfig config;
    
    public P2PService(HttpClient httpClient, BomaPayConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
}