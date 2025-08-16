package org.abutimartin.bpcpayment;

import org.abutimartin.bpcpayment.test.TestConfiguration;
import org.abutimartin.bpcpayment.test.TestCards;

public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("🚀 BomaPay SDK Test Runner");
        System.out.println("==========================");
        
        System.out.println("\n📋 Available Test Cards:");
        printTestCards();
        
        System.out.println("\n🔧 Test Configuration:");
        printTestConfiguration();
        
        System.out.println("\n▶️ To run tests, use:");
        System.out.println("mvn test");
        System.out.println("mvn test -Dtest=OrderServiceTest");
        System.out.println("mvn test -Dtest=PaymentServiceTest");
        System.out.println("mvn test -Dtest=PaymentFlowIntegrationTest");
        
        System.out.println("\n🏷️ Test by tags:");
        System.out.println("mvn test -Dgroups=integration");
    }
    
    private static void printTestCards() {
        System.out.println("\n✅ SUCCESS Cards:");
        printCard("3DS2 Full", TestCards.SuccessCards.VISA_3DS2_FULL);
        printCard("3DS2 Frictionless", TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS);
        printCard("3DS2 Attempt", TestCards.SuccessCards.VISA_3DS2_ATTEMPT);
        printCard("SSL", TestCards.SuccessCards.VISA_SSL);
        
        System.out.println("\n❌ FAILURE Cards:");
        printCard("3DS2 Failure", TestCards.FailureCards.MASTERCARD_3DS2_FAILURE);
    }
    
    private static void printCard(String name, TestCards.TestCard card) {
        System.out.printf("   %-20s | %s | %s/%s | %s%n", 
            name, 
            maskPan(card.getPan()), 
            card.getCvc(), 
            card.getExpiry(),
            card.getAuthType().getDescription());
    }
    
    private static String maskPan(String pan) {
        if (pan.length() < 8) return pan;
        return pan.substring(0, 4) + " **** " + pan.substring(pan.length() - 4);
    }
    
    private static void printTestConfiguration() {
        System.out.println("Base URL: " + TestConfiguration.SANDBOX_BASE_URL);
        System.out.println("Username: " + TestConfiguration.TEST_USERNAME);
        System.out.println("Client ID: " + TestConfiguration.TEST_CLIENT_ID);
        System.out.println("Currency: " + TestConfiguration.DEFAULT_CURRENCY + " (EUR)");
        System.out.println("Language: " + TestConfiguration.DEFAULT_LANGUAGE);
    }
}