package com.pyqportal.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic paperUploadedTopic() {
        return TopicBuilder.name("paper.uploaded")
                .partitions(1)
                .replicas(1)
                .build();
    }

    /** Used by PaperIndexConsumer to download PDF bytes from Cloudinary URL */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
