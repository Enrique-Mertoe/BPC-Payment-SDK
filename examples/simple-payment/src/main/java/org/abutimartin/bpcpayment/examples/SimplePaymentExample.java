package org.abutimartin.bpcpayment.examples;

import org.abutimartin.bpcpayment.BomaPayClient;
import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.model.response.OrderRegistrationResponse;
import org.abutimartin.bpcpayment.model.response.PaymentResponse;
import org.abutimartin.bpcpayment.test.TestCards;
import org.abutimartin.bpcpayment.test.TestConfiguration;

/**
 * Simple example demonstrating basic payment processing with BomaPay SDK.
 * 
 * This example shows how to:
 * 1. Configure the SDK for sandbox testing
 * 2. Register a payment order
 * 3. Process an instant payment
 * 4. Handle success and error responses
 */
public class SimplePaymentExample {
    
    public static void main(String[] args) {
        System.out.println("🚀 BomaPay SDK - Simple Payment Example");
        System.out.println("========================================\n");
        
        try {
            // Step 1: Create SDK configuration for sandbox testing
            BomaPayConfig config = BomaPayConfig.builder()
                    .username("test_user")
                    .password("test_user_password")
                    .clientId("259753456")
                    .merchantLogin("OurBestMerchantLogin")
                    .baseUrl("https://dev.bpcbt.com/payment")
                    .currency("978") // EUR
                    .language("en")
                    .timeout(30000)
                    .build();
            
            // Step 2: Create BomaPay client
            BomaPayClient client = new BomaPayClient(config);
            
            // Step 3: Register a payment order
            System.out.println("📋 Step 1: Registering payment order...");
            String orderNumber = "EXAMPLE-" + System.currentTimeMillis();
            Long amount = 10000L; // 100.00 EUR in minor units
            String returnUrl = "https://mystore.com/payment/return";
            
            OrderRegistrationResponse orderResponse = client.orders()
                    .register(amount, orderNumber, returnUrl);
            
            if (orderResponse.isSuccess()) {
                System.out.println("✅ Order registered successfully!");
                System.out.println("   Order ID: " + orderResponse.getOrderId());
                System.out.println("   Payment Form URL: " + orderResponse.getFormUrl());
            } else {
                System.out.println("❌ Order registration failed: " + orderResponse.getErrorMessage());
                return;
            }
            
            // Step 4: Process instant payment with test card
            System.out.println("\n💳 Step 2: Processing instant payment...");
            TestCards.TestCard testCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS;
            
            PaymentResponse paymentResponse = client.payments().instantPayment(
                    amount,
                    orderNumber + "-PAYMENT",
                    "Example payment for demonstration",
                    testCard.getPan(),
                    testCard.getCvc(),
                    testCard.getExpiryFormatted(),
                    testCard.getCardholderName(),
                    returnUrl,
                    "https://mystore.com/payment/failure"
            );
            
            if (paymentResponse.isSuccess()) {
                System.out.println("✅ Payment processed successfully!");
                System.out.println("   Payment Order ID: " + paymentResponse.getOrderId());
                System.out.println("   Order Status: " + paymentResponse.getOrderStatus());
                System.out.println("   Amount: " + paymentResponse.getAmount() + " (minor units)");
                System.out.println("   Currency: " + paymentResponse.getCurrency());
                
                // Check if 3DS authentication is required
                if (paymentResponse.getAcsUrl() != null) {
                    System.out.println("   3DS Authentication required:");
                    System.out.println("   ACS URL: " + paymentResponse.getAcsUrl());
                }
            } else {
                System.out.println("❌ Payment failed: " + paymentResponse.getErrorMessage());
                System.out.println("   Error Code: " + paymentResponse.getErrorCode());
            }
            
            // Step 5: Demonstrate error handling with invalid card
            System.out.println("\n🔍 Step 3: Demonstrating error handling...");
            TestCards.TestCard invalidCard = testCard.withIncorrectCvc();
            
            PaymentResponse errorResponse = client.payments().instantPayment(
                    amount,
                    orderNumber + "-ERROR",
                    "Payment with invalid CVC",
                    invalidCard.getPan(),
                    invalidCard.getCvc(), // Wrong CVC (999)
                    invalidCard.getExpiryFormatted(),
                    invalidCard.getCardholderName(),
                    returnUrl,
                    "https://mystore.com/payment/failure"
            );
            
            if (!errorResponse.isSuccess()) {
                System.out.println("✅ Error handling working correctly!");
                System.out.println("   Expected error: " + errorResponse.getErrorMessage());
                System.out.println("   Error Code: " + errorResponse.getErrorCode());
            } else {
                System.out.println("⚠️ Unexpected success with invalid card");
            }
            
            System.out.println("\n🎉 Example completed successfully!");
            System.out.println("\nNext steps:");
            System.out.println("- Review the Spring Boot example for web application integration");
            System.out.println("- Check the testing utilities for comprehensive testing");
            System.out.println("- Read the documentation for advanced features");
            
        } catch (BomaPayException e) {
            System.err.println("❌ BomaPay SDK Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}