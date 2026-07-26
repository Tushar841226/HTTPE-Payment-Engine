package com.httpe.auth.controller;

import com.httpe.auth.entity.Payment;
import com.httpe.auth.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }
    @PutMapping("/{id}")
    public Payment updatePaymentStatus(
        @PathVariable Long id,
        @RequestParam String status) {
        return paymentService.updatePaymentStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public String deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return "Payment Deleted Successfully";
    }
}