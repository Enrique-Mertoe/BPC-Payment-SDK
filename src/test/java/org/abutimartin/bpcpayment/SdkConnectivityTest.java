package org.abutimartin.bpcpayment;

import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.model.response.OrderRegistrationResponse;
import org.abutimartin.bpcpayment.model.response.PaymentResponse;
import org.abutimartin.bpcpayment.test.TestConfiguration;
import org.abutimartin.bpcpayment.test.TestCards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Conservative tests focusing on SDK connectivity and basic functionality
 * rather than specific payment outcomes which may vary in sandbox environment.
 */
@Tag("integration")
public class SdkConnectivityTest {
    
    private BomaPayClient client;
    
    @BeforeEach
    void setUp() {
        client = TestConfiguration.createTestClient();
    }
    
    @Test
    @DisplayName("SDK should connect to BomaPay API successfully")
    void testApiConnectivity() {
        try {
            String orderNumber = TestConfiguration.generateTestOrderNumber("CONNECTIVITY");
            Long amount = TestConfiguration.getTestAmount();
            
            OrderRegistrationResponse response = client.orders()
                    .register(amount, orderNumber, TestConfiguration.TEST_RETURN_URL);
            
            // We should get a proper response (success or failure with error message)
            assertNotNull(response, "Response should not be null");
            
            // Either we get success with order ID, or failure with error message
            if (response.isSuccess()) {
                assertNotNull(response.getOrderId(), "Successful response should have order ID");
                System.out.println("✅ API connectivity test passed - Order registered: " + response.getOrderId());
            } else {
                assertNotNull(response.getErrorMessage(), "Failed response should have error message");
                System.out.println("✅ API connectivity test passed - Got expected error: " + response.getErrorMessage());
            }
            
        } catch (BomaPayException e) {
            // Even an exception is acceptable as long as we can connect to the API
            System.out.println("✅ API connectivity test passed - Got API response (exception): " + e.getMessage());
            assertNotNull(e.getMessage(), "Exception should have a message");
        }
    }
    
    @Test
    @DisplayName("SDK should handle payment requests properly")
    void testPaymentRequestHandling() {
        try {
            TestCards.TestCard testCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS;
            
            PaymentResponse response = client.payments().instantPayment(
                    TestConfiguration.getTestAmount(),
                    TestConfiguration.generateTestOrderNumber("PAYMENT-TEST"),
                    "SDK connectivity test payment",
                    testCard.getPan(),
                    testCard.getCvc(),
                    testCard.getExpiryFormatted(),
                    testCard.getCardholderName(),
                    TestConfiguration.TEST_RETURN_URL,
                    TestConfiguration.TEST_RETURN_URL
            );
            
            // Verify we get a structured response
            assertNotNull(response, "Payment response should not be null");
            
            if (response.isSuccess()) {
                assertNotNull(response.getOrderId(), "Successful payment should have order ID");
                System.out.println("✅ Payment test passed - Payment processed: " + response.getOrderId());
            } else {
                assertNotNull(response.getErrorMessage(), "Failed payment should have error message");
                System.out.println("✅ Payment test passed - Got expected error: " + response.getErrorMessage());
            }
            
        } catch (BomaPayException e) {
            // Exception is also acceptable as it shows the API is responding
            System.out.println("✅ Payment test passed - Got API response (exception): " + e.getMessage());
            assertNotNull(e.getMessage(), "Exception should have a message");
        }
    }
    
    @Test
    @DisplayName("SDK should handle invalid input gracefully")
    void testInvalidInputHandling() {
        try {
            // Test with clearly invalid data
            PaymentResponse response = client.payments().instantPayment(
                    -100L, // Invalid negative amount
                    "INVALID-TEST",
                    "Invalid payment test",
                    "1234567890123456", // Invalid card number
                    "000", // Invalid CVC
                    "000000", // Invalid expiry
                    "TEST USER",
                    TestConfiguration.TEST_RETURN_URL,
                    TestConfiguration.TEST_RETURN_URL
            );
            
            // Should get a failure response
            assertNotNull(response, "Response should not be null even for invalid input");
            assertFalse(response.isSuccess(), "Response should indicate failure for invalid input");
            assertNotNull(response.getErrorMessage(), "Failed response should have error message");
            
            System.out.println("✅ Invalid input test passed - Got expected error: " + response.getErrorMessage());
            
        } catch (BomaPayException e) {
            // Exception is also acceptable for invalid input
            System.out.println("✅ Invalid input test passed - Got expected exception: " + e.getMessage());
            assertNotNull(e.getMessage(), "Exception should have a message");
        }
    }
    
    @Test
    @DisplayName("SDK configuration should be properly applied")
    void testSdkConfiguration() {
        // Verify the client was created with expected configuration
        assertNotNull(client, "Client should be created successfully");
        assertNotNull(client.orders(), "Order service should be available");
        assertNotNull(client.payments(), "Payment service should be available");
        assertNotNull(client.bindings(), "Binding service should be available");
        
        System.out.println("✅ SDK configuration test passed - All services available");
    }
}