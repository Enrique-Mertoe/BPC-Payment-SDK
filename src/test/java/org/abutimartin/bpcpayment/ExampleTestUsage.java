package org.abutimartin.bpcpayment;

import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.model.response.OrderRegistrationResponse;
import org.abutimartin.bpcpayment.model.response.PaymentResponse;
import org.abutimartin.bpcpayment.test.TestConfiguration;
import org.abutimartin.bpcpayment.test.TestCards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Example test class demonstrating how to use the BomaPay SDK testing utilities.
 * This class serves as documentation and can be copied as a starting point for your own tests.
 */
public class ExampleTestUsage {
    
    private BomaPayClient client;
    
    @BeforeEach
    void setUp() {
        // Create a test client with pre-configured sandbox settings
        client = TestConfiguration.createTestClient();
    }
    
    @Test
    @DisplayName("Example: Basic order registration")
    void exampleBasicOrderRegistration() throws BomaPayException {
        // Generate unique order number
        String orderNumber = TestConfiguration.generateTestOrderNumber("EXAMPLE");
        
        // Use standard test amount (100.00)
        Long amount = TestConfiguration.getTestAmount();
        
        // Register the order
        OrderRegistrationResponse response = client.orders()
                .register(amount, orderNumber, TestConfiguration.TEST_RETURN_URL);
        
        // Verify the response
        assertTrue(response.isSuccess(), "Order registration should succeed");
        assertNotNull(response.getOrderId(), "Order ID should be present");
        
        System.out.println("✅ Order registered: " + response.getOrderId());
    }
    
    @Test
    @DisplayName("Example: Successful payment with 3DS2 Frictionless card")
    void exampleSuccessfulPayment() throws BomaPayException {
        // Use a pre-configured success test card
        TestCards.TestCard testCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS;
        
        // Process the payment
        PaymentResponse response = client.payments().instantPayment(
                TestConfiguration.getTestAmount(),
                TestConfiguration.generateTestOrderNumber("SUCCESS"),
                "Example successful payment",
                testCard.getPan(),
                testCard.getCvc(),
                testCard.getExpiryFormatted(),
                testCard.getCardholderName(),
                TestConfiguration.TEST_RETURN_URL,
                TestConfiguration.TEST_RETURN_URL
        );
        
        // Verify successful payment
        assertTrue(response.isSuccess(), "Payment should succeed with valid test card");
        assertNotNull(response.getOrderId(), "Order ID should be present");
        
        System.out.println("✅ Payment successful: " + response.getOrderId());
        System.out.println("Card used: " + testCard.getDescription());
    }
    
    @Test
    @DisplayName("Example: Expected failure with wrong CVC")
    void exampleFailureWrongCvc() throws BomaPayException {
        // Create a test card with incorrect CVC
        TestCards.TestCard wrongCvcCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS
                .withIncorrectCvc();
        
        // Process the payment (should fail)
        PaymentResponse response = client.payments().instantPayment(
                TestConfiguration.getTestAmount(),
                TestConfiguration.generateTestOrderNumber("FAIL-CVC"),
                "Example failed payment - wrong CVC",
                wrongCvcCard.getPan(),
                wrongCvcCard.getCvc(),  // This will be "999"
                wrongCvcCard.getExpiryFormatted(),
                wrongCvcCard.getCardholderName(),
                TestConfiguration.TEST_RETURN_URL,
                TestConfiguration.TEST_RETURN_URL
        );
        
        // Verify payment failed as expected
        assertFalse(response.isSuccess(), "Payment should fail with wrong CVC");
        assertTrue(response.getErrorCode().equals("71015") || 
                  response.getErrorMessage().contains("Decline"),
                  "Should return decline error");
        
        System.out.println("❌ Expected failure with wrong CVC:");
        System.out.println("Error: " + response.getErrorMessage());
    }
    
    @Test
    @DisplayName("Example: Testing different card types")
    void exampleTestingDifferentCardTypes() {
        // Test different authorization types
        TestCards.TestCard[] cardsToTest = {
            TestCards.SuccessCards.VISA_3DS2_FULL,        // 3DS2 Full
            TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS, // 3DS2 Frictionless  
            TestCards.SuccessCards.VISA_3DS2_ATTEMPT,     // 3DS2 Attempt
            TestCards.SuccessCards.VISA_SSL,              // SSL
            TestCards.FailureCards.MASTERCARD_3DS2_FAILURE // Expected failure
        };
        
        for (TestCards.TestCard card : cardsToTest) {
            try {
                System.out.println("\n--- Testing: " + card.getDescription() + " ---");
                
                PaymentResponse response = client.payments().instantPayment(
                        TestConfiguration.getTestAmount(),
                        TestConfiguration.generateTestOrderNumber(card.getAuthType().name()),
                        "Testing " + card.getDescription(),
                        card.getPan(),
                        card.getCvc(),
                        card.getExpiryFormatted(),
                        card.getCardholderName(),
                        TestConfiguration.TEST_RETURN_URL,
                        TestConfiguration.TEST_RETURN_URL
                );
                
                boolean expectedSuccess = card.getExpectedResult() == TestCards.ResultType.SUCCESS;
                String result = response.isSuccess() ? "✅ SUCCESS" : "❌ FAILED";
                String expected = expectedSuccess ? "(Expected)" : "(Expected)";
                
                System.out.println(result + " " + expected);
                
                if (response.isSuccess()) {
                    System.out.println("Order ID: " + response.getOrderId());
                } else {
                    System.out.println("Error: " + response.getErrorMessage());
                }
                
                // Assert based on expected result
                assertEquals(expectedSuccess, response.isSuccess(), 
                    "Result should match expectation for " + card.getDescription());
                
            } catch (BomaPayException e) {
                boolean expectedFailure = card.getExpectedResult() == TestCards.ResultType.FAILURE;
                System.out.println("❌ EXCEPTION " + (expectedFailure ? "(Expected)" : "(Unexpected)") + 
                                 ": " + e.getMessage());
                
                if (!expectedFailure) {
                    fail("Unexpected exception for " + card.getDescription() + ": " + e.getMessage());
                }
            }
        }
    }
    
    @Test
    @DisplayName("Example: Custom amount testing")
    void exampleCustomAmountTesting() throws BomaPayException {
        // Test with different amounts
        Double[] testAmounts = {1.00, 10.50, 99.99, 500.00};
        TestCards.TestCard testCard = TestCards.SuccessCards.VISA_SSL;
        
        for (Double amount : testAmounts) {
            Long amountInMinorUnits = TestConfiguration.getTestAmount(amount);
            
            PaymentResponse response = client.payments().instantPayment(
                    amountInMinorUnits,
                    TestConfiguration.generateTestOrderNumber("AMOUNT"),
                    "Testing amount: " + amount,
                    testCard.getPan(),
                    testCard.getCvc(),
                    testCard.getExpiryFormatted(),
                    testCard.getCardholderName(),
                    TestConfiguration.TEST_RETURN_URL,
                    TestConfiguration.TEST_RETURN_URL
            );
            
            System.out.printf("Amount: %.2f -> %s%n", 
                amount, 
                response.isSuccess() ? "✅ SUCCESS" : "❌ FAILED: " + response.getErrorMessage());
        }
    }
    
    @Test
    @DisplayName("Example: Error handling patterns")
    void exampleErrorHandling() {
        try {
            // This should fail - negative amount
            client.orders().register(-100L, "INVALID", TestConfiguration.TEST_RETURN_URL);
            fail("Should have thrown exception for negative amount");
            
        } catch (BomaPayException e) {
            System.out.println("✅ Properly caught negative amount error: " + e.getMessage());
        }
        
        try {
            // This should fail - invalid order ID for refund
            client.orders().refund("INVALID-ORDER-ID", 100L);
            fail("Should have thrown exception for invalid order ID");
            
        } catch (BomaPayException e) {
            System.out.println("✅ Properly caught invalid order ID error: " + e.getMessage());
        }
    }
}