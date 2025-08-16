package org.abutimartin.bpcpayment.test;

public class TestCards {
    
    public static final class SuccessCards {
        public static final TestCard VISA_3DS2_FULL = new TestCard(
            "5555555555555599", "123", "1234", "3DS2 Full Success", 
            AuthorizationType.THREE_DS2, ResultType.SUCCESS, FlowType.FULL
        );
        
        public static final TestCard VISA_3DS2_FRICTIONLESS = new TestCard(
            "4111111111111111", "123", "1226", "3DS2 Frictionless Success",
            AuthorizationType.THREE_DS2, ResultType.SUCCESS, FlowType.FRICTIONLESS
        );
        
        public static final TestCard VISA_3DS2_ATTEMPT = new TestCard(
            "4000001111111118", "123", "1230", "3DS2 Attempt Success",
            AuthorizationType.THREE_DS2_ATTEMPT, ResultType.SUCCESS, FlowType.FRICTIONLESS
        );
        
        public static final TestCard VISA_SSL = new TestCard(
            "4444555511113333", "123", "1226", "SSL Success",
            AuthorizationType.SSL, ResultType.SUCCESS, FlowType.FRICTIONLESS
        );
    }
    
    public static final class FailureCards {
        public static final TestCard MASTERCARD_3DS2_FAILURE = new TestCard(
            "5168494895055780", "123", "1226", "3DS2 Failure",
            AuthorizationType.THREE_DS2, ResultType.FAILURE, FlowType.FRICTIONLESS
        );
    }
    
    public static class TestCard {
        private final String pan;
        private final String cvc;
        private final String expiry;
        private final String description;
        private final AuthorizationType authType;
        private final ResultType expectedResult;
        private final FlowType flowType;
        
        public TestCard(String pan, String cvc, String expiry, String description,
                       AuthorizationType authType, ResultType expectedResult, FlowType flowType) {
            this.pan = pan;
            this.cvc = cvc;
            this.expiry = expiry;
            this.description = description;
            this.authType = authType;
            this.expectedResult = expectedResult;
            this.flowType = flowType;
        }
        
        public String getPan() { return pan; }
        public String getCvc() { return cvc; }
        public String getExpiry() { return expiry; }
        public String getExpiryFormatted() { return "20" + expiry; }
        public String getDescription() { return description; }
        public AuthorizationType getAuthType() { return authType; }
        public ResultType getExpectedResult() { return expectedResult; }
        public FlowType getFlowType() { return flowType; }
        
        public String getCardholderName() { return "TEST CARDHOLDER"; }
        
        public TestCard withIncorrectCvc() {
            return new TestCard(this.pan, "999", this.expiry, this.description + " (Wrong CVC)",
                              this.authType, ResultType.FAILURE, this.flowType);
        }
        
        public TestCard withIncorrectExpiry() {
            return new TestCard(this.pan, this.cvc, "0123", this.description + " (Wrong Expiry)",
                              this.authType, ResultType.FAILURE, this.flowType);
        }
        
        @Override
        public String toString() {
            return String.format("TestCard{pan='%s', description='%s', expected=%s}", 
                               maskPan(), description, expectedResult);
        }
        
        private String maskPan() {
            if (pan.length() < 8) return pan;
            return pan.substring(0, 4) + "****" + pan.substring(pan.length() - 4);
        }
    }
    
    public enum AuthorizationType {
        THREE_DS2("3DS2"),
        THREE_DS2_ATTEMPT("3DS2 Attempt"),
        SSL("SSL");
        
        private final String description;
        
        AuthorizationType(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    public enum ResultType {
        SUCCESS, FAILURE
    }
    
    public enum FlowType {
        FULL, FRICTIONLESS
    }
}