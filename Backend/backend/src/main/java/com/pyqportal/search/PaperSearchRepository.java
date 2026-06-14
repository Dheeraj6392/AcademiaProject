package com.pyqportal.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PaperSearchRepository extends ElasticsearchRepository<PaperDocument, String> {
}
