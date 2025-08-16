package org.abutimartin.bpcpayment;

import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.exception.BomaPayException;
import org.abutimartin.bpcpayment.model.response.OrderRegistrationResponse;
import org.abutimartin.bpcpayment.model.response.PaymentResponse;

public class Main {
    public static void main(String[] args) {
        try {
            BomaPayConfig config = BomaPayConfig.builder()
                    .username("test_user")
                    .password("test_user_password")
                    .clientId("259753456")
                    .merchantLogin("OurBestMerchantLogin")
                    .baseUrl("https://dev.bpcbt.com/payment")
                    .build();
            
            BomaPayClient client = new BomaPayClient(config);
            
            System.out.println("=== BomaPay SDK Example ===");
            
            Long amount = 10000L;
            String orderNumber = "ORDER-" + System.currentTimeMillis();
            String returnUrl = "https://mybestmerchantreturnurl.com";
            
            OrderRegistrationResponse orderResponse = client.orders()
                    .register(amount, orderNumber, returnUrl);
            
            if (orderResponse.isSuccess()) {
                System.out.println("Order registered successfully!");
                System.out.println("Order ID: " + orderResponse.getOrderId());
                System.out.println("Form URL: " + orderResponse.getFormUrl());
            } else {
                System.out.println("Order registration failed: " + orderResponse.getErrorMessage());
            }
            
            PaymentResponse instantPayment = client.payments()
                    .instantPayment(
                            amount,
                            orderNumber + "-INSTANT",
                            "Test payment",
                            "4000001111111118",
                            "123",
                            "203012",
                            "TEST CARDHOLDER",
                            returnUrl,
                            returnUrl
                    );
            
            if (instantPayment.isSuccess()) {
                System.out.println("Instant payment created successfully!");
                System.out.println("Payment Order ID: " + instantPayment.getOrderId());
            } else {
                System.out.println("Instant payment failed: " + instantPayment.getErrorMessage());
            }
            
        } catch (BomaPayException e) {
            System.err.println("BomaPay Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}