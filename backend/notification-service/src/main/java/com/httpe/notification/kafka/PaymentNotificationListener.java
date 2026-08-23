package com.httpe.notification.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.httpe.notification.dto.PaymentNotification;

@Service
public class PaymentNotificationListener {

    @KafkaListener(
            topics = "payment-topic",
            groupId = "notification-group"
    )
    public void consumePayment(PaymentNotification payment) {

        System.out.println("=================================");
        System.out.println("Payment received from Kafka");
        System.out.println("Payment ID: " + payment.getId());
        System.out.println("Sender: " + payment.getSenderName());
        System.out.println("Receiver: " + payment.getReceiverName());
        System.out.println("Amount: " + payment.getAmount());
        System.out.println("Status: " + payment.getStatus());
        System.out.println("=================================");
    }
}