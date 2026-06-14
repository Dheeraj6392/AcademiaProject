package com.pyqportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Explicit repository scan boundaries prevent Spring Data JPA and Spring Data Elasticsearch
 * from accidentally trying to manage each other's repositories when both starters are present.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {
        "com.pyqportal.user",
        "com.pyqportal.paper",
        "com.pyqportal.subscription",
        "com.pyqportal.course"
})
@EnableElasticsearchRepositories(basePackages = "com.pyqportal.search")
public class PYQPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(PYQPortalApplication.class, args);
    }
}
