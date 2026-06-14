package com.pyqportal.config;

import org.springframework.context.annotation.Configuration;

/**
 * Elasticsearch is auto-configured from application.yml (spring.elasticsearch.uris).
 * Repository scanning is handled by @EnableElasticsearchRepositories on PYQPortalApplication.
 */
@Configuration
public class ElasticsearchConfig {
    // No additional beans needed for default single-node setup
}
