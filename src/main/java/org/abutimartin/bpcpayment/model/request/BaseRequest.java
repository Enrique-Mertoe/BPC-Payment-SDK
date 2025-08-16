package org.abutimartin.bpcpayment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseRequest {
    @JsonProperty("userName")
    private String userName;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("language")
    private String language = "en";
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
}