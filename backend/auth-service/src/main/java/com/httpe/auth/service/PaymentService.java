package com.httpe.auth.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.httpe.auth.dto.PaymentRequest;
import com.httpe.auth.dto.PaymentResponse;
import com.httpe.auth.entity.Payment;
import com.httpe.auth.exception.ResourceNotFoundException;
import com.httpe.auth.kafka.PaymentProducer;
import com.httpe.auth.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;
    private final RedisService redisService;

    public PaymentService(
            PaymentRepository paymentRepository,
            PaymentProducer paymentProducer,
            RedisService redisService) {

        this.paymentRepository = paymentRepository;
        this.paymentProducer = paymentProducer;
        this.redisService = redisService;
    }

    // =========================
    // CREATE PAYMENT
    // =========================
    @CacheEvict(value = "payments", allEntries = true)
    public PaymentResponse createPayment(PaymentRequest request) {

        Payment payment = new Payment();

        payment.setSenderName(request.getSenderName());
        payment.setReceiverName(request.getReceiverName());
        payment.setAmount(request.getAmount());

        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        // Save into MySQL
        Payment savedPayment = paymentRepository.save(payment);

        // Send to Kafka
        paymentProducer.sendPayment(savedPayment);

        // Save into Redis
        redisService.save(
                "payment:" + savedPayment.getId(),
                savedPayment,
                60
        );

        System.out.println(
                "Payment saved in Redis with key: payment:"
                        + savedPayment.getId()
        );

        return mapToResponse(savedPayment);
    }

    // =========================
    // GET ALL PAYMENTS
    // =========================
    @Cacheable(value = "payments")
    public List<Payment> getAllPayments() {

        System.out.println("Fetching payments from DATABASE...");

        return paymentRepository.findAll();
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================
    public Payment getPaymentById(Long id) {

        String redisKey = "payment:" + id;

        // First check Redis
        Object cachedPayment = redisService.get(redisKey);

        if (cachedPayment != null) {

            System.out.println(
                    "Fetching payment " + id + " from REDIS..."
            );

            return (Payment) cachedPayment;
        }

        // If not found in Redis, fetch from MySQL
        System.out.println(
                "Payment " + id + " not found in Redis."
        );

        System.out.println(
                "Fetching payment " + id + " from DATABASE..."
        );

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + id
                        )
                );

        // Save database result into Redis
        redisService.save(
                redisKey,
                payment,
                60
        );

        System.out.println(
                "Payment " + id + " saved into REDIS."
        );

        return payment;
    }

    // =========================
    // UPDATE PAYMENT STATUS
    // =========================
    @CacheEvict(value = {"payments", "payment"}, allEntries = true)
    public Payment updatePaymentStatus(Long id, String status) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + id
                        )
                );

        payment.setStatus(status);

        Payment updatedPayment = paymentRepository.save(payment);

        // Update Redis
        redisService.save(
                "payment:" + id,
                updatedPayment,
                60
        );

        System.out.println(
                "Updated payment saved in REDIS: payment:" + id
        );

        return updatedPayment;
    }

    // =========================
    // DELETE PAYMENT
    // =========================
    @CacheEvict(value = {"payments", "payment"}, allEntries = true)
    public void deletePayment(Long id) {

        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Payment not found with id: " + id
            );
        }

        paymentRepository.deleteById(id);

        // Remove from Redis
        redisService.delete("payment:" + id);

        System.out.println(
                "Payment deleted from REDIS: payment:" + id
        );
    }

    // =========================
    // MAP PAYMENT TO RESPONSE
    // =========================
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