package com.httpe.auth.controller;

import com.httpe.auth.entity.Payment;
import com.httpe.auth.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {

    System.out.println("========== PAYMENT API HIT ==========");
    System.out.println("Sender: " + payment.getSenderName());
    System.out.println("Receiver: " + payment.getReceiverName());
    System.out.println("Amount: " + payment.getAmount());

    return paymentService.createPayment(payment);
}

    @GetMapping("/{id}")
    public Optional<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }
}