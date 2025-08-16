package org.abutimartin.bpcpayment.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseResponse {
    @JsonProperty("errorCode")
    private String errorCode;
    
    @JsonProperty("errorMessage")
    private String errorMessage;
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public boolean isSuccess() {
        return "0".equals(errorCode);
    }
}