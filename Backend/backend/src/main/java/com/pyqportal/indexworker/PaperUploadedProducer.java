package com.pyqportal.indexworker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaperUploadedProducer {

    private static final String TOPIC = "paper.uploaded";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(PaperUploadedEvent event) {
        kafkaTemplate.send(TOPIC, event.paperId(), event);
        log.info("Published PaperUploadedEvent for paperId={}", event.paperId());
    }
}
