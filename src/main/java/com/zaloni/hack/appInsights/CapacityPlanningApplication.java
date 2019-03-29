package com.zaloni.hack.appInsights;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaloni.hack.appInsights.dto.Insight;
import com.zaloni.hack.appInsights.service.ESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class CapacityPlanningApplication implements ApplicationRunner {

    @Autowired
    ESService esService;

	public static void main(String[] args) {
		SpringApplication.run(CapacityPlanningApplication.class, args);


	}

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassLoader clazz= getClass().getClassLoader();

        //Fetching the json from resources, for now.
        //This should call zdp apis and format the json to index in ES.
        String insightFile= clazz.getResource("insight.json").getFile();
        Insight insight=new ObjectMapper().readValue(insightFile, Insight.class);
        System.out.println("Saving Document to ES: "+insight.toString());
        esService.save(insight);
    }
}
