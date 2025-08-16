package org.abutimartin.bpcpayment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstantPaymentRequest extends BaseRequest {
    @JsonProperty("amount")
    private Long amount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("orderNumber")
    private String orderNumber;
    
    @JsonProperty("pan")
    private String pan;
    
    @JsonProperty("cvc")
    private String cvc;
    
    @JsonProperty("expiry")
    private String expiry;
    
    @JsonProperty("cardHolderName")
    private String cardHolderName;
    
    @JsonProperty("backUrl")
    private String backUrl;
    
    @JsonProperty("failUrl")
    private String failUrl;
    
    public Long getAmount() {
        return amount;
    }
    
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public String getPan() {
        return pan;
    }
    
    public void setPan(String pan) {
        this.pan = pan;
    }
    
    public String getCvc() {
        return cvc;
    }
    
    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
    
    public String getExpiry() {
        return expiry;
    }
    
    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
    
    public String getCardHolderName() {
        return cardHolderName;
    }
    
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
    
    public String getBackUrl() {
        return backUrl;
    }
    
    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }
    
    public String getFailUrl() {
        return failUrl;
    }
    
    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }
}