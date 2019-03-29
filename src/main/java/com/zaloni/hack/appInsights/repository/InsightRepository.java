package com.zaloni.hack.appInsights.repository;

import com.zaloni.hack.appInsights.dto.Insight;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface InsightRepository extends ElasticsearchRepository<Insight, String> {
}
