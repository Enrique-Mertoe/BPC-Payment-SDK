package org.abutimartin.bpcpayment.config;

public class BomaPayConfig {
    private final String baseUrl;
    private final String username;
    private final String password;
    private final String clientId;
    private final String merchantLogin;
    private final String language;
    private final String currency;
    private final int timeout;
    
    private BomaPayConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.username = builder.username;
        this.password = builder.password;
        this.clientId = builder.clientId;
        this.merchantLogin = builder.merchantLogin;
        this.language = builder.language;
        this.currency = builder.currency;
        this.timeout = builder.timeout;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public String getMerchantLogin() {
        return merchantLogin;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String baseUrl = "https://dev.bpcbt.com/payment";
        private String username;
        private String password;
        private String clientId;
        private String merchantLogin;
        private String language = "en";
        private String currency = "978";
        private int timeout = 30000;
        
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
        
        public Builder merchantLogin(String merchantLogin) {
            this.merchantLogin = merchantLogin;
            return this;
        }
        
        public Builder language(String language) {
            this.language = language;
            return this;
        }
        
        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }
        
        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
        
        public BomaPayConfig build() {
            if (username == null || password == null) {
                throw new IllegalArgumentException("Username and password are required");
            }
            return new BomaPayConfig(this);
        }
    }
}