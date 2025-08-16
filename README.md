# BomaPay SDK for Java/Kotlin

A comprehensive Java SDK for integrating with the BomaPay (BPC) payment gateway. This SDK provides easy-to-use APIs for processing payments, managing orders, and handling various payment methods including card payments, Apple Pay, Google Pay, and Samsung Pay.

## Features

- 🔒 Secure payment processing
- 💳 Support for card payments (instant, MOTO, pre-auth)
- 📱 Mobile wallet integration (Apple Pay, Google Pay, Samsung Pay)
- 🔄 P2P transfers
- 🗂️ Stored credential management
- ✅ Order lifecycle management
- 🛡️ Built-in validation and error handling
- 📊 Comprehensive logging

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.abutimartin.bpcpayment</groupId>
    <artifactId>BPPaymentSDK</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Gradle

Add the following to your `build.gradle`:

```gradle
implementation 'org.abutimartin.bpcpayment:BPPaymentSDK:1.0-SNAPSHOT'
```

## Quick Start

### 1. Configuration

```java
import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.BomaPayClient;

BomaPayConfig config = BomaPayConfig.builder()
    .username("your_api_username")
    .password("your_api_password")
    .clientId("your_client_id")
    .merchantLogin("your_merchant_login")
    .baseUrl("https://dev.bpcbt.com/payment") // or production URL
    .currency("978") // EUR by default
    .language("en")
    .timeout(30000) // 30 seconds
    .build();

BomaPayClient client = new BomaPayClient(config);
```

### 2. Register an Order

```java
try {
    OrderRegistrationResponse response = client.orders()
        .register(
            10000L, // Amount in minor units (100.00)
            "ORDER-12345", // Your order number
            "https://your-site.com/return" // Return URL
        );
    
    if (response.isSuccess()) {
        System.out.println("Order ID: " + response.getOrderId());
        System.out.println("Payment Form URL: " + response.getFormUrl());
    } else {
        System.out.println("Error: " + response.getErrorMessage());
    }
} catch (BomaPayException e) {
    e.printStackTrace();
}
```

### 3. Process Instant Payment

```java
try {
    PaymentResponse response = client.payments()
        .instantPayment(
            10000L,                    // Amount
            "ORDER-12345",             // Order number
            "Payment description",     // Description
            "4000001111111118",        // Card number
            "123",                     // CVC
            "203012",                  // Expiry (YYYYMM)
            "JOHN SMITH",              // Cardholder name
            "https://your-site.com/success", // Success URL
            "https://your-site.com/fail"     // Failure URL
        );
    
    if (response.isSuccess()) {
        System.out.println("Payment successful!");
        System.out.println("Order ID: " + response.getOrderId());
    }
} catch (BomaPayException e) {
    e.printStackTrace();
}
```

## API Reference

### Order Management

#### Register Order
```java
OrderRegistrationResponse response = client.orders()
    .register(amount, orderNumber, returnUrl);
```

#### Register Pre-Authorization
```java
OrderRegistrationResponse response = client.orders()
    .registerPreAuth(amount, orderNumber, returnUrl);
```

#### Deposit (Complete Pre-Auth)
```java
BaseResponse response = client.orders()
    .deposit(orderId, amount);
```

#### Refund
```java
BaseResponse response = client.orders()
    .refund(orderId, amount);
```

#### Reverse Payment
```java
BaseResponse response = client.orders()
    .reverse(orderId);
```

#### Decline Order
```java
BaseResponse response = client.orders()
    .decline(orderId, orderNumber);
```

### Payment Processing

#### Payment for Existing Order
```java
PaymentResponse response = client.payments()
    .paymentOrder(mdOrder, pan, cvc, year, month, cardholderName);
```

#### MOTO Payment
```java
PaymentResponse response = client.payments()
    .motoPayment(amount, description, pan, expiry, cvc, cardholder, returnUrl);
```

### Stored Credentials (Bindings)

#### Get Customer Bindings
```java
BaseResponse response = client.bindings()
    .getBindings(clientId);
```

#### Get Bindings by Card
```java
BaseResponse response = client.bindings()
    .getBindingsByCardOrId(panMask);
```

#### Deactivate Binding
```java
BaseResponse response = client.bindings()
    .unBindCard(bindingId);
```

#### Activate Binding
```java
BaseResponse response = client.bindings()
    .bindCard(bindingId);
```

#### Extend Binding Expiry
```java
BaseResponse response = client.bindings()
    .extendBinding(bindingId, "203212"); // YYYYMM
```

## Spring Boot Integration

For Spring Boot applications, you can create a configuration class:

```java
@Configuration
public class BomaPayConfiguration {

    @Value("${bomapay.username}")
    private String username;
    
    @Value("${bomapay.password}")
    private String password;
    
    @Value("${bomapay.client-id}")
    private String clientId;
    
    @Value("${bomapay.merchant-login}")
    private String merchantLogin;
    
    @Value("${bomapay.base-url}")
    private String baseUrl;

    @Bean
    public BomaPayClient bomaPayClient() {
        BomaPayConfig config = BomaPayConfig.builder()
            .username(username)
            .password(password)
            .clientId(clientId)
            .merchantLogin(merchantLogin)
            .baseUrl(baseUrl)
            .build();
            
        return new BomaPayClient(config);
    }
}
```

Then inject it into your services:

```java
@Service
public class PaymentService {
    
    @Autowired
    private BomaPayClient bomaPayClient;
    
    public void processPayment(PaymentRequest request) {
        try {
            OrderRegistrationResponse response = bomaPayClient.orders()
                .register(request.getAmount(), request.getOrderNumber(), request.getReturnUrl());
            // Handle response
        } catch (BomaPayException e) {
            // Handle error
        }
    }
}
```

## Error Handling

All SDK methods throw `BomaPayException` for payment-related errors:

```java
try {
    OrderRegistrationResponse response = client.orders().register(amount, orderNumber, returnUrl);
} catch (BomaPayException e) {
    // Log error
    logger.error("Payment error: {}", e.getMessage());
    
    // Handle different types of errors
    if (e.getMessage().contains("HTTP error: 401")) {
        // Authentication error
    } else if (e.getMessage().contains("HTTP error: 400")) {
        // Bad request
    }
}
```

## Validation Utilities

The SDK includes validation utilities for common payment data:

```java
import org.abutimartin.bpcpayment.util.ValidationUtil;
import org.abutimartin.bpcpayment.util.CurrencyUtil;

// Validate payment data
boolean isValidPan = ValidationUtil.isValidPan("4000001111111118");
boolean isValidCvc = ValidationUtil.isValidCvc("123");
boolean isValidExpiry = ValidationUtil.isValidExpiry("203012");

// Convert currency
Long minorUnits = CurrencyUtil.convertToMinorUnits(100.50, "EUR"); // 10050
Double majorUnits = CurrencyUtil.convertFromMinorUnits(10050L, "EUR"); // 100.50
```

## Currency Support

The SDK supports multiple currencies with ISO 4217 numeric codes:

- USD (840)
- EUR (978) - Default
- GBP (826)
- RUB (643)
- KZT (398)
- UZS (860)
- MWK (454) - Malawi Kwacha

## Testing

The SDK includes comprehensive testing capabilities with official BomaPay test cards and scenarios.

### Test Cards

The SDK provides pre-configured test cards for different scenarios:

#### ✅ Success Cards

| Card Number | CVC | Expiry | Authorization | Flow | Result |
|-------------|-----|--------|---------------|------|---------|
| 5555 5555 5555 5599 | 123 | 12/34 | 3DS2 | Full | Success |
| 4111 1111 1111 1111 | 123 | 12/26 | 3DS2 | Frictionless | Success |
| 4000 0011 1111 1118 | 123 | 12/30 | 3DS2 Attempt | - | Success |
| 4444 5555 1111 3333 | 123 | 12/26 | SSL | - | Success |

#### ❌ Failure Cards

| Card Number | CVC | Expiry | Authorization | Flow | Result |
|-------------|-----|--------|---------------|------|---------|
| 5168 4948 9505 5780 | 123 | 12/26 | 3DS2 | Frictionless | Failure |

### Using Test Cards in Code

```java
import org.abutimartin.bpcpayment.test.TestCards;
import org.abutimartin.bpcpayment.test.TestConfiguration;

// Use pre-configured test cards
TestCards.TestCard successCard = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS;
TestCards.TestCard failureCard = TestCards.FailureCards.MASTERCARD_3DS2_FAILURE;

// Create test client
BomaPayClient client = TestConfiguration.createTestClient();

// Process payment with test card
PaymentResponse response = client.payments().instantPayment(
    TestConfiguration.getTestAmount(),
    TestConfiguration.generateTestOrderNumber(),
    "Test payment",
    successCard.getPan(),
    successCard.getCvc(),
    successCard.getExpiryFormatted(),
    successCard.getCardholderName(),
    TestConfiguration.TEST_RETURN_URL,
    TestConfiguration.TEST_RETURN_URL
);
```

### Test Scenarios

#### 1. Successful Payment Flow
```java
@Test
void testSuccessfulPayment() {
    TestCards.TestCard card = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS;
    BomaPayClient client = TestConfiguration.createTestClient();
    
    // Register order
    OrderRegistrationResponse order = client.orders()
        .register(10000L, "TEST-ORDER-123", TestConfiguration.TEST_RETURN_URL);
    
    assertTrue(order.isSuccess());
    assertNotNull(order.getOrderId());
    
    // Process payment
    PaymentResponse payment = client.payments().instantPayment(
        10000L, "PAYMENT-123", "Test payment",
        card.getPan(), card.getCvc(), card.getExpiryFormatted(),
        card.getCardholderName(),
        TestConfiguration.TEST_RETURN_URL,
        TestConfiguration.TEST_RETURN_URL
    );
    
    assertTrue(payment.isSuccess());
}
```

#### 2. Failed Payment with Wrong CVC
```java
@Test
void testFailedPaymentWrongCvc() {
    TestCards.TestCard card = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS
        .withIncorrectCvc(); // This creates card with CVC "999"
    
    PaymentResponse response = client.payments().instantPayment(
        10000L, "FAIL-TEST-123", "Test failed payment",
        card.getPan(), card.getCvc(), card.getExpiryFormatted(),
        card.getCardholderName(),
        TestConfiguration.TEST_RETURN_URL,
        TestConfiguration.TEST_RETURN_URL
    );
    
    assertFalse(response.isSuccess());
    assertEquals("71015", response.getErrorCode()); // Decline error
}
```

#### 3. Failed Payment with Wrong Expiry
```java
@Test
void testFailedPaymentWrongExpiry() {
    TestCards.TestCard card = TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS
        .withIncorrectExpiry(); // This creates card with expired date
    
    // Payment should fail with decline error (71015)
    PaymentResponse response = client.payments().instantPayment(/* ... */);
    assertFalse(response.isSuccess());
}
```

### Running Tests

#### Maven Commands

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=OrderServiceTest
mvn test -Dtest=PaymentServiceTest
mvn test -Dtest=PaymentFlowIntegrationTest

# Run tests by tag
mvn test -Dgroups=integration

# Run test runner utility
mvn exec:java -Dexec.mainClass="org.abutimartin.bpcpayment.TestRunner"
```

#### IDE Testing

Most IDEs support JUnit 5 tests out of the box. Simply:
1. Right-click on test class → Run
2. Use test tags to filter: `@Tag("integration")`
3. Run individual test methods

#### Test Output Example

```
=== TESTING ALL CARD TYPES ===

--- Testing: 3DS2 Frictionless Success ---
✅ SUCCESS (Expected SUCCESS)
Order ID: 12345678-abcd-ef01-2345-123456789abc
🎯 Result matches expectation

--- Testing: 3DS2 Failure ---
❌ FAILED (Expected FAILURE)
Error: Decline. Input error
🎯 Result matches expectation
```

### Test Configuration

#### Environment Variables

For custom test environments, set these environment variables:

```bash
export BOMAPAY_TEST_USERNAME="your_test_username"
export BOMAPAY_TEST_PASSWORD="your_test_password"
export BOMAPAY_TEST_BASE_URL="https://your-test-environment.com"
export BOMAPAY_TEST_CLIENT_ID="your_test_client_id"
```

#### Test Properties

Create `src/test/resources/test.properties`:

```properties
bomapay.test.username=test_user
bomapay.test.password=test_user_password
bomapay.test.base-url=https://dev.bpcbt.com/payment
bomapay.test.client-id=259753456
bomapay.test.merchant-login=OurBestMerchantLogin
```

### Sandbox Environment

Default sandbox configuration:

```java
BomaPayConfig config = BomaPayConfig.builder()
    .username("test_user")
    .password("test_user_password")
    .baseUrl("https://dev.bpcbt.com/payment")
    .clientId("259753456")
    .merchantLogin("OurBestMerchantLogin")
    .build();
```

## Security Best Practices

1. **Never log sensitive data**: Card numbers, CVCs, and passwords
2. **Use HTTPS**: Always use secure connections in production
3. **Validate inputs**: Use the provided validation utilities
4. **Handle errors gracefully**: Don't expose internal error details to end users
5. **Store credentials securely**: Use environment variables or secure vaults

## Logging

The SDK uses SLF4J for logging. Configure your logging framework to capture SDK logs:

```xml
<!-- logback.xml -->
<logger name="org.abutimartin.bpcpayment" level="DEBUG" />
```

## Support

- **Issues**: Report bugs and feature requests on GitHub
- **Documentation**: Check the official BPC payment gateway documentation
- **Examples**: See the `Main.java` class for usage examples

## License

This SDK is provided under the MIT License. See LICENSE file for details.