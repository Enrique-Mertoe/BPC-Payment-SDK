package org.abutimartin.bpcpayment.examples.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for BomaPay SDK.
 */
@ConfigurationProperties(prefix = "bomapay")
public class BomaPayProperties {

    private String username;
    private String password;
    private String clientId;
    private String merchantLogin;
    private String baseUrl = "https://dev.bpcbt.com/payment";
    private String currency = "978";
    private String language = "en";
    private int timeout = 30000;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMerchantLogin() {
        return merchantLogin;
    }

    public void setMerchantLogin(String merchantLogin) {
        this.merchantLogin = merchantLogin;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}