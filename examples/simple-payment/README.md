# Simple Payment Example

This example demonstrates basic payment processing using the BomaPay SDK.

## What This Example Shows

1. **SDK Configuration**: Setting up the SDK for sandbox testing
2. **Order Registration**: Creating a payment order
3. **Instant Payment**: Processing a payment with test card data
4. **Error Handling**: Handling payment failures and errors
5. **Response Processing**: Working with success and error responses

## Running the Example

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Steps

1. Navigate to the example directory:
   ```bash
   cd examples/simple-payment
   ```

2. Run the example:
   ```bash
   mvn compile exec:java
   ```

   Or with Maven wrapper:
   ```bash
   ./mvnw compile exec:java
   ```

3. View the output to see the payment flow in action.

## Example Output

```
🚀 BomaPay SDK - Simple Payment Example
========================================

📋 Step 1: Registering payment order...
✅ Order registered successfully!
   Order ID: 12345678-abcd-ef01-2345-123456789abc
   Payment Form URL: https://dev.bpcbt.com/payment/form/...

💳 Step 2: Processing instant payment...
✅ Payment processed successfully!
   Payment Order ID: 87654321-dcba-10fe-5432-987654321fed
   Order Status: 2
   Amount: 10000 (minor units)
   Currency: 978

🔍 Step 3: Demonstrating error handling...
✅ Error handling working correctly!
   Expected error: Decline. Input error
   Error Code: 71015

🎉 Example completed successfully!
```

## Code Walkthrough

### 1. SDK Configuration

```java
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
```

### 2. Order Registration

```java
OrderRegistrationResponse orderResponse = client.orders()
    .register(amount, orderNumber, returnUrl);
```

### 3. Instant Payment

```java
PaymentResponse paymentResponse = client.payments().instantPayment(
    amount,
    orderNumber,
    "Payment description",
    testCard.getPan(),
    testCard.getCvc(),
    testCard.getExpiryFormatted(),
    testCard.getCardholderName(),
    returnUrl,
    failureUrl
);
```

### 4. Error Handling

```java
if (paymentResponse.isSuccess()) {
    // Handle success
    System.out.println("Payment ID: " + paymentResponse.getOrderId());
} else {
    // Handle error
    System.out.println("Error: " + paymentResponse.getErrorMessage());
}
```

## Test Cards Used

- **Success Card**: `4111111111111111` (Visa 3DS2 Frictionless)
- **Failure Card**: Same card with incorrect CVC `999`

## Key Learning Points

1. **Configuration**: Always use sandbox credentials for testing
2. **Error Handling**: Check `isSuccess()` before processing responses
3. **Test Cards**: Use official BomaPay test cards for reliable testing
4. **Amounts**: Amounts are specified in minor units (cents)
5. **Security**: Never log sensitive card data in production

## Next Steps

- Try the Spring Boot example for web application integration
- Explore the comprehensive test suite
- Review the API documentation for advanced features
- Implement proper logging and monitoring in your application