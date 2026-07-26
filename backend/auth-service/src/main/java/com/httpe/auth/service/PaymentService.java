package com.httpe.auth.service;

import com.httpe.auth.entity.Payment;
import com.httpe.auth.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import com.httpe.auth.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }
    public Payment updatePaymentStatus(Long id, String status) {

    Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

    payment.setStatus(status);

    return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {

    if (!paymentRepository.existsById(id)) {
        throw new ResourceNotFoundException("Payment not found");
    }

    paymentRepository.deleteById(id);
}
}