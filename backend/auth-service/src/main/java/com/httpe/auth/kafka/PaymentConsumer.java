package com.httpe.auth.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.httpe.auth.entity.Payment;

@Service
public class PaymentConsumer {

    @KafkaListener(
        topics = "payment-topic",
        groupId = "payment-group"
    )
    public void consumePayment(Payment payment) {

        System.out.println("Payment received from Kafka:");

        System.out.println("Payment ID: " + payment.getId());
        System.out.println("Sender: " + payment.getSenderName());
        System.out.println("Receiver: " + payment.getReceiverName());
        System.out.println("Amount: " + payment.getAmount());
        System.out.println("Status: " + payment.getStatus());
    }
}