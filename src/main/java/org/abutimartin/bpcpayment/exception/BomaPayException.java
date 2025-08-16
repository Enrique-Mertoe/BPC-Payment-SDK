package org.abutimartin.bpcpayment.exception;

public class BomaPayException extends Exception {
    public BomaPayException(String message) {
        super(message);
    }
    
    public BomaPayException(String message, Throwable cause) {
        super(message, cause);
    }
}