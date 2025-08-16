package org.abutimartin.bpcpayment.examples.springboot.controller;

import org.abutimartin.bpcpayment.BomaPayClient;
import org.abutimartin.bpcpayment.examples.springboot.dto.PaymentRequest;
import org.abutimartin.bpcpayment.examples.springboot.dto.PaymentResult;
import org.abutimartin.bpcpayment.examples.springboot.service.PaymentService;
import org.abutimartin.bpcpayment.test.TestCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Web controller for payment processing demonstration.
 */
@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Shows the payment form.
     */
    @GetMapping
    public String showPaymentForm(Model model) {
        model.addAttribute("paymentRequest", new PaymentRequest());
        model.addAttribute("testCards", getTestCards());
        return "payment/form";
    }

    /**
     * Processes the payment.
     */
    @PostMapping("/process")
    public String processPayment(@ModelAttribute PaymentRequest request, Model model) {
        try {
            PaymentResult result = paymentService.processPayment(request);
            model.addAttribute("result", result);
            return "payment/result";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("paymentRequest", request);
            model.addAttribute("testCards", getTestCards());
            return "payment/form";
        }
    }

    /**
     * API endpoint for processing payments (JSON).
     */
    @PostMapping("/api/process")
    @ResponseBody
    public PaymentResult processPaymentApi(@RequestBody PaymentRequest request) {
        return paymentService.processPayment(request);
    }

    private TestCardInfo[] getTestCards() {
        return new TestCardInfo[]{
            new TestCardInfo("Visa 3DS2 Frictionless (Success)", 
                           TestCards.SuccessCards.VISA_3DS2_FRICTIONLESS),
            new TestCardInfo("Visa 3DS2 Full (Success)", 
                           TestCards.SuccessCards.VISA_3DS2_FULL),
            new TestCardInfo("Visa SSL (Success)", 
                           TestCards.SuccessCards.VISA_SSL),
            new TestCardInfo("Mastercard 3DS2 (Failure)", 
                           TestCards.FailureCards.MASTERCARD_3DS2_FAILURE)
        };
    }

    public static class TestCardInfo {
        private final String name;
        private final TestCards.TestCard card;

        public TestCardInfo(String name, TestCards.TestCard card) {
            this.name = name;
            this.card = card;
        }

        public String getName() { return name; }
        public String getPan() { return card.getPan(); }
        public String getCvc() { return card.getCvc(); }
        public String getExpiry() { return card.getExpiry(); }
        public String getCardholderName() { return card.getCardholderName(); }
    }
}