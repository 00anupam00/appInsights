package com.zaloni.hack.appInsights.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.zaloni.hack.appInsights.dto.*;
import com.zaloni.hack.appInsights.util.JSONAPIResponse;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocGenService {

    public Insight getEditLogWFExecuteEntries(String url,int numOfEntries, String startTime, String endTime) throws Exception {
        try(FileWriter fw = new FileWriter("testout.txt")) {
            HttpResponse<String> loginResponse = Unirest.post(url + "/bedrock-app/services/rest/login")
                    .header("Content-Type", "application/json")
                    .header("cache-control", "no-cache")
                    .header("Postman-Token", "945ac112-faec-4935-856d-aa9f1c11190d")
                    .body("{\n\t\"username\":\"admin\",\n\t\"password\":\"admin\"\n}")
                    .asString();

            HttpResponse<String> rawResponse = Unirest.post(url + "/bedrock-app/services/rest/admin/auditTypes/log/search")
                    .header("Content-Type", "application/json")
                    .header("cache-control", "no-cache")
                    .header("Postman-Token", "a4ab4067-8650-4d5b-91b9-c496bd267bce")
                    .body("{\"auditTrail\": {\"userId\": \"\",\"logTime\": null,\"logType\": \"EXECUTE\",\"auditTrailFields\": []},\"dateFilter\": {\"fromDateTime\": \"\",\"toDateTime\": \"\"},\"page\": {\"currentPage\": 1,\"chunkSize\": " + numOfEntries + ",\"sortBy\": \"logTime\",\"sortOrder\": \"DESC\"}}")
                    .asString();
            //System.out.println("######## Raw response:" + rawResponse.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            JSONAPIResponse<List<AuditTrail>> response = objectMapper.readValue(rawResponse.getBody(), new TypeReference<JSONAPIResponse<List<AuditTrail>>>() {
            });
            //System.out.println("######## AuditTrail " + response.getResult().toString());
            List<AuditTrail> auditTrails = response.getResult();
            if (auditTrails == null || auditTrails.isEmpty()) throw new Exception("No audit trail detected");
            for (AuditTrail auditTrail : auditTrails) {
                DateTime logTime = auditTrail.getLogTime();
                int projectId = auditTrail.getProjectId();
                String executedBy = null;
                int wfInstanceId = 0;
                List<AuditTrailField> auditTrailFields = auditTrail.getAuditTrailFields();
                for (AuditTrailField trailField : auditTrailFields) {
                    String keyName = trailField.getTechnicalName();
                    String value = trailField.getValue();
                    if (keyName.equalsIgnoreCase("executedBy")) {
                        executedBy = value;
                    } else if (keyName.equalsIgnoreCase("instanceId")) {
                        wfInstanceId = Integer.parseInt(value);
                    }
                }

                //fetch Main WF
                Insight insight = fetchWFDetail(url, wfInstanceId, projectId, logTime.getMillis());
                if (insight == null) {
                    continue;
                }
                insight.setInstanceId(wfInstanceId);
                insight.setProjectId(projectId);
                insight.setExecutedBy(executedBy);
                insight.setLogTime(logTime.toString());//TODO: Decide the format
                fw.write("######## Insight Detail: " + insight + "\n\n=============================\n\n");

                return insight;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new Insight();
    }

    private Insight fetchWFDetail(String url, int instanceId, int projectId, long logTime) throws UnirestException, IOException {

        Insight insight = new Insight();
        ObjectMapper objectMapper = new ObjectMapper();

        //get wf id, name and status
        HttpResponse<String> response = Unirest.get(url + "/bedrock-app/services/rest/workflows/instances/" + instanceId + "/status?projectIds=" + projectId)
                .header("cache-control", "no-cache")
                .header("Postman-Token", "1c9d6a3e-5813-4f03-b83e-5f71c6687c1e")
                .asString();

        if(response.getStatus()!=200){
            return null;
        }

        Map<Object, Object> responseObject = objectMapper.readValue(response.getBody(), new TypeReference<Map<Object, Object>>(){});
        System.out.println("#### WF details " + objectMapper.writeValueAsString(responseObject));
        System.out.println("#### WF ID payload:" + objectMapper.writeValueAsString(responseObject.get("result")));
        String wfIDResponse = objectMapper.writeValueAsString(responseObject.get("result"));
        if(wfIDResponse == null || wfIDResponse.isEmpty()){
            return null;
        }
        Map<Object, Object> wfIdMap = objectMapper.readValue(wfIDResponse, new TypeReference<Map<Object, Object>>(){});
        if(wfIdMap == null) return null;
        String wfIdString = objectMapper.writeValueAsString(wfIdMap.get("wfId"));
        if(wfIdString == null || wfIdString.isEmpty()) return null;
        int wfId = Integer.parseInt(wfIdString);
        System.out.println("#### WF ID:" + objectMapper.writeValueAsString(wfIdMap.get("wfId")));
        String wfName = objectMapper.writeValueAsString(wfIdMap.get("wfName"));
        String wfStatus = objectMapper.writeValueAsString(wfIdMap.get("wfStatus")).toUpperCase();

        insight.setWfId(wfId);
        insight.setWfName(wfName);
        insight.setStatus(wfStatus);

        insight.setInsightId(idGenerator(instanceId, wfId, logTime));



        //Get action and entity details

        HttpResponse<String> wfResponse = Unirest.get(url + "/bedrock-app/services/rest/workflows/" + wfId + "?projectIds=" + projectId + "&instanceId=" + instanceId + "&version=1")
                .header("cache-control", "no-cache")
                .header("Postman-Token", "5b629e06-5c9e-4d5a-91ee-ca32dfff568c")
                .asString();

        responseObject = objectMapper.readValue(wfResponse.getBody(), new TypeReference<Map<Object, Object>>(){});
        String wfDetailsResponse = objectMapper.writeValueAsString(responseObject.get("result"));
        System.out.println("####### WF DETAILS:"+ wfDetailsResponse);
        Map<Object, Object> wfDetailsMap = objectMapper.readValue(wfDetailsResponse, new TypeReference<Map<Object, Object>>(){});

        String stepList = objectMapper.writeValueAsString(wfDetailsMap.get("stepList"));

        System.out.println("####### STEP DETAILS:"+ stepList);

        List<Object> steps = objectMapper.readValue(stepList, new TypeReference<List<Object>>(){});

        List<Action> actions = new ArrayList<>();
        for(Object step : steps){
            String stepDetailString = objectMapper.writeValueAsString(step);
            Map<Object, Object> stepDetail = objectMapper.readValue(stepDetailString, new TypeReference<Map<Object, Object>>(){});
            Action action = new Action();
            action.setActionName(objectMapper.writeValueAsString(stepDetail.get("stepType")));
            action.setActionId(objectMapper.writeValueAsString(stepDetail.get("actionId")));
            //TODO: action.setActionStartTime()
            //TODO: action.endData();
            String stepParamListString = objectMapper.writeValueAsString(stepDetail.get("stepParamList"));
            List<Object> stepParams = objectMapper.readValue(stepParamListString, new TypeReference<List<Object>>(){});
            for(Object stepParam : stepParams){
                Map<Object, Object> stepParamMap = objectMapper.readValue(objectMapper.writeValueAsString(stepParam), new TypeReference<Map<Object, Object>>(){});
                if(String.valueOf(stepParamMap.get("key")).equalsIgnoreCase("entityTypeAndVersion")){
                    formatEntityName(action, String.valueOf(stepParamMap.get("valueText"))); //format is entityName(<entityId>.<version>)
                }
            }
            actions.add(action);

        }
        insight.setActions(actions);
        return insight;
    }

  /*  public static void main(String[] args) throws Exception {
        DocGenService docGen = new DocGenService();
        docGen.getEditLogWFExecuteEntries("http://192.168.1.36:9090", 20, "", "");
        //docGen.formatEntityName(null, "ENTITY(10.2)");
    }*/

    private void formatEntityName(Action action, String entityName){
        int entityId= Integer.parseInt(entityName.substring(entityName.lastIndexOf("(")+1, entityName.lastIndexOf(".")));
        int entityVersion= Integer.parseInt(entityName.substring(entityName.lastIndexOf(".")+1, entityName.lastIndexOf(")")));
        String entityTypeName= entityName.substring(0, entityName.lastIndexOf("("));
/*
        System.out.println("Formatted Entity details: ");
        System.out.println("entityId: "+entityId);
        System.out.println("entityVersion: "+entityVersion);
        System.out.println("entityTypeName: "+entityTypeName);*/

        action.setEntityName(entityTypeName);
        action.setEntityId(entityId);
        action.setEntityVersion(entityVersion);
    }

    private Long idGenerator(int instanceId,  int wfId, long logTime) {
        String id= new StringBuilder(instanceId)
                .append(wfId)
                .append(logTime != 0 ? logTime : "000000").toString();
        return Long.parseLong(id);
    }
}
