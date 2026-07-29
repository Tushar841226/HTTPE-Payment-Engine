package com.httpe.auth.service;

import com.httpe.auth.entity.Payment;
import com.httpe.auth.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import com.httpe.auth.exception.ResourceNotFoundException;
import com.httpe.auth.dto.PaymentRequest;
import com.httpe.auth.dto.PaymentResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse createPayment(PaymentRequest request) {

    Payment payment = new Payment();

    payment.setSenderName(request.getSenderName());
    payment.setReceiverName(request.getReceiverName());
    payment.setAmount(request.getAmount());

    payment.setStatus("SUCCESS");
    payment.setPaymentDate(LocalDateTime.now());

    Payment savedPayment = paymentRepository.save(payment);

    return mapToResponse(savedPayment);
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
    private PaymentResponse mapToResponse(Payment payment) {
    PaymentResponse response = new PaymentResponse();

    response.setId(payment.getId());
    response.setSenderName(payment.getSenderName());
    response.setReceiverName(payment.getReceiverName());
    response.setAmount(payment.getAmount());
    response.setStatus(payment.getStatus());
    response.setPaymentDate(payment.getPaymentDate());

    return response;
}
      
}