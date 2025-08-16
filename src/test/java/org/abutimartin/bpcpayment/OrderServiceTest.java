package org.abutimartin.bpcpayment;

import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.model.response.OrderRegistrationResponse;
import org.abutimartin.bpcpayment.model.response.BaseResponse;
import org.abutimartin.bpcpayment.test.TestConfiguration;
import org.abutimartin.bpcpayment.test.TestCards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class OrderServiceTest {
    
    private BomaPayClient client;
    
    @BeforeEach
    void setUp() {
        client = TestConfiguration.createTestClient();
    }
    
    @Test
    @DisplayName("Should successfully register a new order")
    void testOrderRegistration_Success() {
        try {
            String orderNumber = TestConfiguration.generateTestOrderNumber("ORDER");
            Long amount = TestConfiguration.getTestAmount();
            
            OrderRegistrationResponse response = client.orders()
                    .register(amount, orderNumber, TestConfiguration.TEST_RETURN_URL);
            
            assertNotNull(response);
            assertTrue(response.isSuccess(), "Order registration should succeed");
            assertNotNull(response.getOrderId(), "Order ID should not be null");
            assertNotNull(response.getFormUrl(), "Form URL should not be null");
            
            System.out.println("Order registered successfully:");
            System.out.println("Order ID: " + response.getOrderId());
            System.out.println("Form URL: " + response.getFormUrl());
            
        } catch (BomaPayException e) {
            fail("Order registration should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Should successfully register a pre-authorization order")
    void testPreAuthOrderRegistration_Success() {
        try {
            String orderNumber = TestConfiguration.generateTestOrderNumber("PREAUTH");
            Long amount = TestConfiguration.getTestAmount();
            
            OrderRegistrationResponse response = client.orders()
                    .registerPreAuth(amount, orderNumber, TestConfiguration.TEST_RETURN_URL);
            
            assertNotNull(response);
            assertTrue(response.isSuccess(), "Pre-auth order registration should succeed");
            assertNotNull(response.getOrderId(), "Order ID should not be null");
            
            System.out.println("Pre-auth order registered successfully:");
            System.out.println("Order ID: " + response.getOrderId());
            
        } catch (BomaPayException e) {
            fail("Pre-auth order registration should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Should fail order registration with invalid amount")
    void testOrderRegistration_InvalidAmount() {
        try {
            String orderNumber = TestConfiguration.generateTestOrderNumber("INVALID");
            Long invalidAmount = -100L;
            
            OrderRegistrationResponse response = client.orders()
                    .register(invalidAmount, orderNumber, TestConfiguration.TEST_RETURN_URL);
            
            assertNotNull(response);
            assertFalse(response.isSuccess(), "Order registration should fail with negative amount");
            assertNotNull(response.getErrorMessage(), "Error message should be present");
            
            System.out.println("Expected failure for invalid amount:");
            System.out.println("Error: " + response.getErrorMessage());
            
        } catch (BomaPayException e) {
            System.out.println("Expected exception for invalid amount: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Should fail order registration with invalid order number")
    void testOrderRegistration_InvalidOrderNumber() {
        try {
            String invalidOrderNumber = ""; // Empty order number
            Long amount = TestConfiguration.getTestAmount();
            
            OrderRegistrationResponse response = client.orders()
                    .register(amount, invalidOrderNumber, TestConfiguration.TEST_RETURN_URL);
            
            assertNotNull(response);
            assertFalse(response.isSuccess(), "Order registration should fail with empty order number");
            
        } catch (BomaPayException e) {
            System.out.println("Expected exception for invalid order number: " + e.getMessage());
        }
    }
}