package com.pyqportal.search;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.pyqportal.paper.Branch;
import com.pyqportal.paper.ExamType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final PaperSearchRepository paperSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * Full-text search with optional filters.
     * Boosts: title^3, subject^2, content^1.
     *
     * @return list of paper IDs found in Elasticsearch (caller fetches full details from PostgreSQL)
     */
    public List<UUID> searchPapers(String queryText, Branch branch, String subject,
                                   Integer year, ExamType examType) {

        List<Query> filters = new ArrayList<>();

        if (branch != null) {
            filters.add(TermQuery.of(t -> t.field("branch").value(branch.name()))._toQuery());
        }
        if (subject != null) {
            filters.add(TermQuery.of(t -> t.field("subject").value(subject))._toQuery());
        }
        if (year != null) {
            filters.add(TermQuery.of(t -> t.field("year").value(year))._toQuery());
        }
        if (examType != null) {
            filters.add(TermQuery.of(t -> t.field("examType").value(examType.name()))._toQuery());
        }

        Query multiMatch = MultiMatchQuery.of(m -> m
                .query(queryText)
                .fields("title^3", "subject^2", "content")
        )._toQuery();

        Query finalQuery = filters.isEmpty()
                ? multiMatch
                : BoolQuery.of(b -> b.must(multiMatch).filter(filters))._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder().withQuery(finalQuery).build();

        return elasticsearchOperations.search(nativeQuery, PaperDocument.class)
                .getSearchHits()
                .stream()
                .map(hit -> UUID.fromString(hit.getId()))
                .toList();
    }

    public void deleteFromIndex(String paperId) {
        paperSearchRepository.deleteById(paperId);
        log.info("Removed paper {} from Elasticsearch index", paperId);
    }
}
