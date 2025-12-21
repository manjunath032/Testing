package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    @Value("${kafka.topic.demo}")
    private String topic;

    @Value("${kafka.group.demo}")
    private String groupId;

    @KafkaListener(topics = "${kafka.topic.demo}", groupId = "${kafka.group.demo}")
    public void consume(String message, Acknowledgment ack) {
        try {
            process(message);
            ack.acknowledge(); // NOW it matters
        } catch (Exception e) {
            // no ack → offset NOT committed
        }
    }


    private void process(String message) {
        System.out.println("Consumed message : " + message);
    }
}
