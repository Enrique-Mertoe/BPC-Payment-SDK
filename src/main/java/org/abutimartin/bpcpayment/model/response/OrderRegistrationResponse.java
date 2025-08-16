package org.abutimartin.bpcpayment.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRegistrationResponse extends BaseResponse {
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("formUrl")
    private String formUrl;
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getFormUrl() {
        return formUrl;
    }
    
    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }
}