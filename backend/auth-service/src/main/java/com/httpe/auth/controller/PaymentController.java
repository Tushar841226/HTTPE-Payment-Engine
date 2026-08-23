package com.httpe.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.httpe.auth.dto.PaymentRequest;
import com.httpe.auth.dto.PaymentResponse;
import com.httpe.auth.entity.Payment;
import com.httpe.auth.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse createPayment(@Valid @RequestBody PaymentRequest request) {

        System.out.println("========== PAYMENT API HITs ==========");
        System.out.println("Sender: " + request.getSenderName());
        System.out.println("Receiver: " + request.getReceiverName());
        System.out.println("Amount: " + request.getAmount());

        return paymentService.createPayment(request);
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
        return "PaymentSSSssss ";
    }

}
