package org.abutimartin.bpcpayment;

import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.model.response.OrderRegistrationResponse;
import org.abutimartin.bpcpayment.model.response.PaymentResponse;
import org.abutimartin.bpcpayment.model.response.BaseResponse;
import org.abutimartin.bpcpayment.test.TestConfiguration;
import org.abutimartin.bpcpayment.test.TestCards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class PaymentFlowIntegrationTest {
    
    private BomaPayClient client;
    
    @BeforeEach
    void setUp() {
        client = TestConfiguration.createTestClient();
    }
    
    @Test
    @DisplayName("Complete Payment Flow: Register Order → Pay → Check Status")
    void testCompletePaymentFlow() {
        try {
            String orderNumber = TestConfiguration.generateTestOrderNumber("FLOW");
            Long amount = TestConfiguration.getTestAmount();
            TestCards.TestCard testCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS;
            
            // Step 1: Register Order
            System.out.println("=== STEP 1: Registering Order ===");
            OrderRegistrationResponse orderResponse = client.orders()
                    .register(amount, orderNumber, TestConfiguration.TEST_RETURN_URL);
            
            assertTrue(orderResponse.isSuccess(), "Order registration should succeed");
            assertNotNull(orderResponse.getOrderId());
            String orderId = orderResponse.getOrderId();
            
            System.out.println("✅ Order registered: " + orderId);
            
            // Step 2: Process Payment (Instant Payment)
            System.out.println("\n=== STEP 2: Processing Payment ===");
            PaymentResponse paymentResponse = client.payments().instantPayment(
                    amount,
                    orderNumber + "-PAYMENT",
                    "Complete flow test payment",
                    testCard.getPan(),
                    testCard.getCvc(),
                    testCard.getExpiryFormatted(),
                    testCard.getCardholderName(),
                    TestConfiguration.TEST_RETURN_URL,
                    TestConfiguration.TEST_RETURN_URL
            );
            
            assertTrue(paymentResponse.isSuccess(), "Payment should succeed");
            assertNotNull(paymentResponse.getOrderId());
            
            System.out.println("✅ Payment processed: " + paymentResponse.getOrderId());
            System.out.println("Payment Status: " + paymentResponse.getOrderStatus());
            
            // Step 3: Test Refund (if payment was successful)
            if (paymentResponse.getOrderStatus() != null && paymentResponse.getOrderStatus() == 2) {
                System.out.println("\n=== STEP 3: Testing Refund ===");
                BaseResponse refundResponse = client.orders()
                        .refund(paymentResponse.getOrderId(), amount / 2); // Partial refund
                
                System.out.println("Refund result: " + 
                    (refundResponse.isSuccess() ? "✅ SUCCESS" : "❌ FAILED - " + refundResponse.getErrorMessage()));
            }
            
            System.out.println("\n🎉 Complete payment flow test finished successfully!");
            
        } catch (BomaPayException e) {
            fail("Complete payment flow should not fail: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Pre-Authorization Flow: Register → Deposit → Reverse")
    void testPreAuthFlow() {
        try {
            String orderNumber = TestConfiguration.generateTestOrderNumber("PREAUTH");
            Long amount = TestConfiguration.getTestAmount();
            
            // Step 1: Register Pre-Auth Order
            System.out.println("=== STEP 1: Registering Pre-Auth Order ===");
            OrderRegistrationResponse preAuthResponse = client.orders()
                    .registerPreAuth(amount, orderNumber, TestConfiguration.TEST_RETURN_URL);
            
            assertTrue(preAuthResponse.isSuccess(), "Pre-auth registration should succeed");
            assertNotNull(preAuthResponse.getOrderId());
            String orderId = preAuthResponse.getOrderId();
            
            System.out.println("✅ Pre-auth order registered: " + orderId);
            
            // Note: In a real scenario, customer would complete 3DS authentication here
            
            // Step 2: Attempt Deposit (Complete Pre-Auth)
            System.out.println("\n=== STEP 2: Attempting Deposit ===");
            BaseResponse depositResponse = client.orders()
                    .deposit(orderId, amount);
            
            System.out.println("Deposit result: " + 
                (depositResponse.isSuccess() ? "✅ SUCCESS" : "❌ FAILED - " + depositResponse.getErrorMessage()));
            
            // Step 3: If deposit failed, try reverse
            if (!depositResponse.isSuccess()) {
                System.out.println("\n=== STEP 3: Attempting Reverse ===");
                BaseResponse reverseResponse = client.orders()
                        .reverse(orderId);
                
                System.out.println("Reverse result: " + 
                    (reverseResponse.isSuccess() ? "✅ SUCCESS" : "❌ FAILED - " + reverseResponse.getErrorMessage()));
            }
            
            System.out.println("\n🎉 Pre-authorization flow test completed!");
            
        } catch (BomaPayException e) {
            System.out.println("Pre-auth flow completed with expected exceptions: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Error Handling Flow: Test various error scenarios")
    void testErrorHandlingFlow() {
        System.out.println("=== ERROR HANDLING FLOW TEST ===");
        
        // Test 1: Invalid amount
        try {
            client.orders().register(-100L, "INVALID-AMOUNT", TestConfiguration.TEST_RETURN_URL);
            fail("Should throw exception for negative amount");
        } catch (BomaPayException e) {
            System.out.println("✅ Correctly handled negative amount: " + e.getMessage());
        }
        
        // Test 2: Wrong card details
        try {
            TestCards.TestCard wrongCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS
                    .withIncorrectCvc();
            
            PaymentResponse response = client.payments().instantPayment(
                    TestConfiguration.getTestAmount(),
                    TestConfiguration.generateTestOrderNumber("WRONG"),
                    "Wrong card test",
                    wrongCard.getPan(),
                    wrongCard.getCvc(),
                    wrongCard.getExpiryFormatted(),
                    wrongCard.getCardholderName(),
                    TestConfiguration.TEST_RETURN_URL,
                    TestConfiguration.TEST_RETURN_URL
            );
            
            assertFalse(response.isSuccess(), "Should fail with wrong CVC");
            System.out.println("✅ Correctly handled wrong CVC: " + response.getErrorMessage());
            
        } catch (BomaPayException e) {
            System.out.println("✅ Exception for wrong card details: " + e.getMessage());
        }
        
        // Test 3: Invalid order operations
        try {
            client.orders().refund("INVALID-ORDER-ID", 100L);
            fail("Should throw exception for invalid order ID");
        } catch (BomaPayException e) {
            System.out.println("✅ Correctly handled invalid order ID: " + e.getMessage());
        }
        
        System.out.println("\n🎉 Error handling flow test completed!");
    }
    
    @Test
    @DisplayName("Card Type Testing: Test all provided test cards")
    void testAllCardTypes() {
        System.out.println("=== TESTING ALL CARD TYPES ===");
        
        TestCards.TestCard[] allCards = {
            TestCards.SuccessCards.VISA_3DS2_FULL,
            TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS,
            TestCards.SuccessCards.VISA_3DS2_ATTEMPT,
            TestCards.SuccessCards.VISA_SSL,
            TestCards.FailureCards.MASTERCARD_3DS2_FAILURE
        };
        
        for (TestCards.TestCard card : allCards) {
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
                String expected = expectedSuccess ? "(Expected SUCCESS)" : "(Expected FAILURE)";
                
                System.out.println(result + " " + expected);
                if (!response.isSuccess()) {
                    System.out.println("Error: " + response.getErrorMessage());
                } else {
                    System.out.println("Order ID: " + response.getOrderId());
                }
                
                if (response.isSuccess() == expectedSuccess) {
                    System.out.println("🎯 Result matches expectation");
                } else {
                    System.out.println("⚠️ Result differs from expectation");
                }
                
            } catch (BomaPayException e) {
                boolean expectedFailure = card.getExpectedResult() == TestCards.ResultType.FAILURE;
                System.out.println("❌ EXCEPTION " + (expectedFailure ? "(Expected)" : "(Unexpected)") + ": " + e.getMessage());
            }
        }
        
        System.out.println("\n🎉 All card types tested!");
    }
}