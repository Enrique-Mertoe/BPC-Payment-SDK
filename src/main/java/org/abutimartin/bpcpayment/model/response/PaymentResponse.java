package org.abutimartin.bpcpayment.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentResponse extends BaseResponse {
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("orderNumber")
    private String orderNumber;
    
    @JsonProperty("orderStatus")
    private Integer orderStatus;
    
    @JsonProperty("amount")
    private Long amount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("redirectUrl")
    private String redirectUrl;
    
    @JsonProperty("acsUrl")
    private String acsUrl;
    
    @JsonProperty("paReq")
    private String paReq;
    
    @JsonProperty("termUrl")
    private String termUrl;
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public Integer getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
    
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
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    public String getAcsUrl() {
        return acsUrl;
    }
    
    public void setAcsUrl(String acsUrl) {
        this.acsUrl = acsUrl;
    }
    
    public String getPaReq() {
        return paReq;
    }
    
    public void setPaReq(String paReq) {
        this.paReq = paReq;
    }
    
    public String getTermUrl() {
        return termUrl;
    }
    
    public void setTermUrl(String termUrl) {
        this.termUrl = termUrl;
    }
}