package org.abutimartin.bpcpayment;

import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.abutimartin.bpcpayment.http.HttpClient;
import org.abutimartin.bpcpayment.service.*;

/**
 * Main entry point for the BomaPay SDK.
 * 
 * <p>This client provides access to all BomaPay payment gateway functionality including:
 * <ul>
 *   <li>Order management (registration, pre-authorization, deposits, refunds)</li>
 *   <li>Payment processing (instant payments, MOTO payments)</li>
 *   <li>Stored credential management (bindings)</li>
 *   <li>Mobile wallet payments (Apple Pay, Google Pay, Samsung Pay)</li>
 *   <li>P2P transfers</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * // Create configuration
 * BomaPayConfig config = BomaPayConfig.builder()
 *     .username("your_api_username")
 *     .password("your_api_password")
 *     .clientId("your_client_id")
 *     .baseUrl("https://api.bomapay.com/payment")
 *     .build();
 * 
 * // Create client
 * BomaPayClient client = new BomaPayClient(config);
 * 
 * // Register an order
 * OrderRegistrationResponse order = client.orders()
 *     .register(10000L, "ORDER-123", "https://return-url.com");
 * 
 * // Process instant payment
 * PaymentResponse payment = client.payments()
 *     .instantPayment(10000L, "ORDER-123", "Payment description",
 *                    "4111111111111111", "123", "203012", "JOHN SMITH",
 *                    "https://success-url.com", "https://fail-url.com");
 * }</pre>
 * 
 * @author Martin Abuti
 * @version 1.0.0
 * @since 1.0.0
 */
public class BomaPayClient {
    private final BomaPayConfig config;
    private final HttpClient httpClient;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final BindingService bindingService;
    private final P2PService p2pService;
    private final ApplePayService applePayService;
    private final GooglePayService googlePayService;
    private final SamsungPayService samsungPayService;
    
    /**
     * Creates a new BomaPay client with the specified configuration.
     * 
     * @param config the configuration containing API credentials and settings
     * @throws IllegalArgumentException if config is null or contains invalid values
     */
    public BomaPayClient(BomaPayConfig config) {
        this.config = config;
        this.httpClient = new HttpClient(config);
        this.orderService = new OrderService(httpClient, config);
        this.paymentService = new PaymentService(httpClient, config);
        this.bindingService = new BindingService(httpClient, config);
        this.p2pService = new P2PService(httpClient, config);
        this.applePayService = new ApplePayService(httpClient, config);
        this.googlePayService = new GooglePayService(httpClient, config);
        this.samsungPayService = new SamsungPayService(httpClient, config);
    }
    
    /**
     * Gets the order service for managing payment orders.
     * 
     * @return the order service instance
     */
    public OrderService orders() {
        return orderService;
    }
    
    /**
     * Gets the payment service for processing payments.
     * 
     * @return the payment service instance
     */
    public PaymentService payments() {
        return paymentService;
    }
    
    /**
     * Gets the binding service for managing stored credentials.
     * 
     * @return the binding service instance
     */
    public BindingService bindings() {
        return bindingService;
    }
    
    /**
     * Gets the P2P service for peer-to-peer transfers.
     * 
     * @return the P2P service instance
     */
    public P2PService p2p() {
        return p2pService;
    }
    
    /**
     * Gets the Apple Pay service for Apple Pay payments.
     * 
     * @return the Apple Pay service instance
     */
    public ApplePayService applePay() {
        return applePayService;
    }
    
    /**
     * Gets the Google Pay service for Google Pay payments.
     * 
     * @return the Google Pay service instance
     */
    public GooglePayService googlePay() {
        return googlePayService;
    }
    
    /**
     * Gets the Samsung Pay service for Samsung Pay payments.
     * 
     * @return the Samsung Pay service instance
     */
    public SamsungPayService samsungPay() {
        return samsungPayService;
    }
}