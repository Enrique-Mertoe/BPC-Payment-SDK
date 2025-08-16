package org.abutimartin.bpcpayment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentOrderRequest extends BaseRequest {
    @JsonProperty("MDORDER")
    private String mdOrder;
    
    @JsonProperty("$PAN")
    private String pan;
    
    @JsonProperty("$CVC")
    private String cvc;
    
    @JsonProperty("YYYY")
    private String year;
    
    @JsonProperty("MM")
    private String month;
    
    @JsonProperty("TEXT")
    private String cardholderName;
    
    public String getMdOrder() {
        return mdOrder;
    }
    
    public void setMdOrder(String mdOrder) {
        this.mdOrder = mdOrder;
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
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    public String getMonth() {
        return month;
    }
    
    public void setMonth(String month) {
        this.month = month;
    }
    
    public String getCardholderName() {
        return cardholderName;
    }
    
    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }
}