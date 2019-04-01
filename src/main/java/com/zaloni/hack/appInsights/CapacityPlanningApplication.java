package com.zaloni.hack.appInsights;

import com.zaloni.hack.appInsights.dto.Insight;
import com.zaloni.hack.appInsights.service.DocGenService;
import com.zaloni.hack.appInsights.service.ESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@EnableScheduling
@SpringBootApplication
public class CapacityPlanningApplication implements ApplicationRunner {

    @Autowired
    ESService esService;

    @Autowired
    DocGenService docGen;

    int currentPage = 1;

    @Value("${zdp_url}")
    String zdp_url;

	public static void main(String[] args) {SpringApplication.run(CapacityPlanningApplication.class, args);}

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassLoader clazz= getClass().getClassLoader();
        //Delete es Index
        //esService.deleteIndex(Insight.class);

        //Fetching the json from resources, for now.
        //This should call zdp apis and format the json to index in ES.
        //String insightFile= clazz.getResource("insight.json").getFile();
        //Insight insight=new ObjectMapper().readValue(new File(insightFile), Insight.class);
        //List<Insight> insights= docGen.getEditLogWFExecuteEntries("http://192.168.1.204:8080", 20, currentPage, "", "");
        System.out.println("##### Saving Document to ES");
        /*for(Insight insight : insights){
            esService.save(insight);
            System.out.println("##### Documents saved successfully:" + insight.getInsightId());
        }
        System.out.println("##### All Documents successfully");*/

    }


    //TODO FIXME for batch processing
    @Scheduled(fixedDelay = 1000L)
    public void sycTask(){
	    if(currentPage > 40) return;
        System.out.println("Started analysing zdp insights...");
	    //Call the zdp and make the Insight Object.
        //Sync to ES.
//        DocGenService docGen = new DocGenService();
        try{
            System.out.println("##### Current page: " + currentPage);
            List<Insight> insights= docGen.getEditLogWFExecuteEntries("http://192.168.1.204:8080", 20, currentPage,"", "");
            insights.forEach(esService::save);
            System.out.println("##### done indexing");
            currentPage++;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("ZDP insights analysed successfully.");
    }


}
