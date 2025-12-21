package org.example;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Profile("!old")
public class KafkaConsumerListener_old {

    @KafkaListener(topics = "demo-topic", groupId = "demo-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }

}
