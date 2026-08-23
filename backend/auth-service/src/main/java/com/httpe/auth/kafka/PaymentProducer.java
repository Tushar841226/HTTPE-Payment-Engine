package com.httpe.auth.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.httpe.auth.entity.Payment;

@Service
public class PaymentProducer {

    private final KafkaTemplate<String, Payment> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, Payment> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPayment(Payment payment) {

        kafkaTemplate.send("payment-topic", payment);

        System.out.println("Payment sent to Kafka: " + payment.getId());
    }
}
