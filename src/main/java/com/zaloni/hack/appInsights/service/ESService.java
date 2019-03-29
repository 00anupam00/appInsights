package com.zaloni.hack.appInsights.service;

import com.zaloni.hack.appInsights.config.ESConfiguration;
import com.zaloni.hack.appInsights.dto.Insight;
import com.zaloni.hack.appInsights.repository.InsightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class ESService {

    @Autowired
    InsightRepository insightRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

   /* public ESService() throws Exception{
        this.elasticsearchTemplate= (ElasticsearchTemplate) new ESConfiguration().elasticsearchTemplate();
    }*/

    public Insight save(Insight insight){
       return insightRepository.save(insight);
   }

}
