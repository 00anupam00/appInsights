package com.zaloni.hack.appInsights.service;

import com.zaloni.hack.appInsights.config.ESConfiguration;
import com.zaloni.hack.appInsights.dto.Insight;
import com.zaloni.hack.appInsights.repository.InsightRepository;
import org.elasticsearch.common.settings.Settings;
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
        Insight savedInsight= new Insight();
        boolean indexExists= this.elasticsearchTemplate.indexExists(Insight.class)
                || this.elasticsearchTemplate.createIndex(Insight.class);
        if(indexExists){
            savedInsight= insightRepository.save(insight);
        }
       return savedInsight;
   }

   public void deleteIndex(Class indexName){
        this.elasticsearchTemplate.deleteIndex(indexName);
   }

}
