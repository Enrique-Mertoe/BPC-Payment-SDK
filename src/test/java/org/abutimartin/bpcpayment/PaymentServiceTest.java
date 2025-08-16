package org.abutimartin.bpcpayment;

import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.model.response.PaymentResponse;
import org.abutimartin.bpcpayment.test.TestConfiguration;
import org.abutimartin.bpcpayment.test.TestCards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class PaymentServiceTest {
    
    private BomaPayClient client;
    
    @BeforeEach
    void setUp() {
        client = TestConfiguration.createTestClient();
    }
    
    @ParameterizedTest
    @MethodSource("successTestCards")
    @DisplayName("Should successfully process instant payment with valid test cards")
    void testInstantPayment_Success(TestCards.TestCard testCard) {
        try {
            String orderNumber = TestConfiguration.generateTestOrderNumber("INSTANT");
            Long amount = TestConfiguration.getTestAmount();
            
            PaymentResponse response = client.payments().instantPayment(
                    amount,
                    orderNumber,
                    "Test payment with " + testCard.getDescription(),
                    testCard.getPan(),
                    testCard.getCvc(),
                    testCard.getExpiryFormatted(),
                    testCard.getCardholderName(),
                    TestConfiguration.TEST_RETURN_URL,
                    TestConfiguration.TEST_RETURN_URL
            );
            
            assertNotNull(response);
            
            if (testCard.getExpectedResult() == TestCards.ResultType.SUCCESS) {
                assertTrue(response.isSuccess(), 
                    "Payment should succeed for card: " + testCard.getDescription());
                assertNotNull(response.getOrderId(), "Order ID should not be null");
                
                System.out.println("✅ SUCCESS: " + testCard.getDescription());
                System.out.println("Order ID: " + response.getOrderId());
                System.out.println("Order Status: " + response.getOrderStatus());
            } else {
                assertFalse(response.isSuccess(), 
                    "Payment should fail for card: " + testCard.getDescription());
                
                System.out.println("❌ EXPECTED FAILURE: " + testCard.getDescription());
                System.out.println("Error: " + response.getErrorMessage());
            }
            
        } catch (BomaPayException e) {
            if (testCard.getExpectedResult() == TestCards.ResultType.FAILURE) {
                System.out.println("❌ EXPECTED EXCEPTION: " + testCard.getDescription() + 
                                 " - " + e.getMessage());
            } else {
                fail("Unexpected exception for " + testCard.getDescription() + ": " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Should fail instant payment with incorrect CVC")
    void testInstantPayment_IncorrectCVC() {
        try {
            TestCards.TestCard testCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS
                    .withIncorrectCvc();
            
            String orderNumber = TestConfiguration.generateTestOrderNumber("WRONG-CVC");
            Long amount = TestConfiguration.getTestAmount();
            
            PaymentResponse response = client.payments().instantPayment(
                    amount,
                    orderNumber,
                    "Test payment with wrong CVC",
                    testCard.getPan(),
                    testCard.getCvc(),
                    testCard.getExpiryFormatted(),
                    testCard.getCardholderName(),
                    TestConfiguration.TEST_RETURN_URL,
                    TestConfiguration.TEST_RETURN_URL
            );
            
            assertNotNull(response);
            assertFalse(response.isSuccess(), "Payment should fail with incorrect CVC");
            assertTrue(response.getErrorCode().equals("71015") || 
                      response.getErrorMessage().contains("Decline"), 
                      "Should return decline error (71015)");
            
            System.out.println("❌ EXPECTED FAILURE - Wrong CVC:");
            System.out.println("Error Code: " + response.getErrorCode());
            System.out.println("Error Message: " + response.getErrorMessage());
            
        } catch (BomaPayException e) {
            System.out.println("❌ EXPECTED EXCEPTION - Wrong CVC: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Should fail instant payment with incorrect expiry date")
    void testInstantPayment_IncorrectExpiry() {
        try {
            TestCards.TestCard testCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS
                    .withIncorrectExpiry();
            
            String orderNumber = TestConfiguration.generateTestOrderNumber("WRONG-EXPIRY");
            Long amount = TestConfiguration.getTestAmount();
            
            PaymentResponse response = client.payments().instantPayment(
                    amount,
                    orderNumber,
                    "Test payment with wrong expiry",
                    testCard.getPan(),
                    testCard.getCvc(),
                    testCard.getExpiryFormatted(),
                    testCard.getCardholderName(),
                    TestConfiguration.TEST_RETURN_URL,
                    TestConfiguration.TEST_RETURN_URL
            );
            
            assertNotNull(response);
            assertFalse(response.isSuccess(), "Payment should fail with incorrect expiry");
            assertTrue(response.getErrorCode().equals("71015") || 
                      response.getErrorMessage().contains("Decline"), 
                      "Should return decline error (71015)");
            
            System.out.println("❌ EXPECTED FAILURE - Wrong Expiry:");
            System.out.println("Error Code: " + response.getErrorCode());
            System.out.println("Error Message: " + response.getErrorMessage());
            
        } catch (BomaPayException e) {
            System.out.println("❌ EXPECTED EXCEPTION - Wrong Expiry: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Should successfully process MOTO payment")
    void testMotoPayment_Success() {
        try {
            TestCards.TestCard testCard = TestCards.SuccessCards.VISA_SSL;
            Long amount = TestConfiguration.getTestAmount();
            
            PaymentResponse response = client.payments().motoPayment(
                    amount,
                    "MOTO Test Payment",
                    testCard.getPan(),
                    testCard.getExpiryFormatted(),
                    testCard.getCvc(),
                    testCard.getCardholderName(),
                    TestConfiguration.TEST_RETURN_URL
            );
            
            assertNotNull(response);
            assertTrue(response.isSuccess(), "MOTO payment should succeed");
            assertNotNull(response.getOrderId(), "Order ID should not be null");
            
            System.out.println("✅ MOTO Payment Success:");
            System.out.println("Order ID: " + response.getOrderId());
            System.out.println("Order Status: " + response.getOrderStatus());
            
        } catch (BomaPayException e) {
            fail("MOTO payment should not throw exception: " + e.getMessage());
        }
    }
    
    static Stream<TestCards.TestCard> successTestCards() {
        return Stream.of(
                TestCards.SuccessCards.VISA_3DS2_FULL,
                TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS,
                TestCards.SuccessCards.VISA_3DS2_ATTEMPT,
                TestCards.SuccessCards.VISA_SSL,
                TestCards.FailureCards.MASTERCARD_3DS2_FAILURE
        );
    }
}