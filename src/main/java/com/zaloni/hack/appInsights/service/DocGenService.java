package com.zaloni.hack.appInsights.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.zaloni.hack.appInsights.dto.Action;
import com.zaloni.hack.appInsights.dto.AuditTrail;
import com.zaloni.hack.appInsights.dto.AuditTrailField;
import com.zaloni.hack.appInsights.dto.Insight;
import com.zaloni.hack.appInsights.util.JSONAPIResponse;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DocGenService {

    String ZDP_LOGIN_URL = "/bedrock-app/services/rest/login";
    String ZDP_AUDIT_LOG_URLS = "/bedrock-app/services/rest/admin/auditTypes/log/search";

    public List<Insight> getEditLogWFExecuteEntries(String url,int numOfEntries, int currentPage, String startTime, String endTime) throws Exception {
        List<Insight> insights = new ArrayList<>();
        try(FileWriter fw = new FileWriter("testout.txt")) {
            HttpResponse<String> loginResponse = Unirest.post(url + ZDP_LOGIN_URL)
                    .header("Content-Type", "application/json")
                    .header("cache-control", "no-cache")
                    .header("Postman-Token", "945ac112-faec-4935-856d-aa9f1c11190d")
                    .body("{\n\t\"username\":\"admin\",\n\t\"password\":\"admin\"\n}")
                    .asString();

            HttpResponse<String> rawResponse = Unirest.post(url + ZDP_AUDIT_LOG_URLS)
                    .header("Content-Type", "application/json")
                    .header("cache-control", "no-cache")
                    .header("Postman-Token", "a4ab4067-8650-4d5b-91b9-c496bd267bce")
                    .body("{\"auditTrail\": {\"userId\": \"\",\"logTime\": null,\"logType\": \"EXECUTE\",\"auditTrailFields\": []},\"dateFilter\": {\"fromDateTime\": \"\",\"toDateTime\": \"\"},\"page\": {\"currentPage\": " + currentPage + ",\"chunkSize\": " + numOfEntries + ",\"sortBy\": \"logTime\",\"sortOrder\": \"DESC\"}}")

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
                insights.add(insight);
                //fw.write("######## Insight Detail: " + insight + "\n\n=============================\n\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return insights;
    }

    private Insight fetchWFDetail(String url, int instanceId, int projectId, long logTime) throws UnirestException, IOException, ParseException {

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
        //System.out.println("#### WF details " + objectMapper.writeValueAsString(responseObject));
        //System.out.println("#### WF ID payload:" + objectMapper.writeValueAsString(responseObject.get("result")));
        String wfIDResponse = objectMapper.writeValueAsString(responseObject.get("result"));
        if(wfIDResponse == null || wfIDResponse.isEmpty()){
            return null;
        }
        Map<Object, Object> wfIdMap = objectMapper.readValue(wfIDResponse, new TypeReference<Map<Object, Object>>(){});
        if(wfIdMap == null) return null;
        String wfIdString = objectMapper.writeValueAsString(wfIdMap.get("wfId"));
        if(wfIdString == null || wfIdString.isEmpty()) return null;
        int wfId = Integer.parseInt(wfIdString);
        //System.out.println("#### WF ID:" + objectMapper.writeValueAsString(wfIdMap.get("wfId")));
        String wfName = objectMapper.writeValueAsString(wfIdMap.get("wfName")).replace("\"", "");
        String wfStatus = objectMapper.writeValueAsString(wfIdMap.get("wfStatus")).toUpperCase();

        //fetch start time of 'Start' action
        //fetch end time of 'End' action
        String wfStartTime = null;
        String wfEndTime = null;
        String listActionStatusString = objectMapper.writeValueAsString(wfIdMap.get("list"));
        List<Object> statusActions = objectMapper.readValue(listActionStatusString, new TypeReference<List<Object>>(){});
        for(Object actionStatus : statusActions){
            Map<Object, Object> stepActionMap = objectMapper.readValue(objectMapper.writeValueAsString(actionStatus), new TypeReference<Map<Object, Object>>(){});
            String actionName = objectMapper.writeValueAsString(stepActionMap.get("stepType")).replace("\"", "");
            String startTime = objectMapper.writeValueAsString(stepActionMap.get("startDate"));
            //System.out.println("###### action, start: "+ actionName + ", " + startTime);
            String endTime = objectMapper.writeValueAsString(stepActionMap.get("endDate"));
            //System.out.println("###### action, stop: " + actionName + ", " + endTime);
            if(actionName.equalsIgnoreCase("start")){
                wfStartTime = objectMapper.writeValueAsString(stepActionMap.get("startDate")).replace("\"", "");
                //System.out.println("###### Start action:" + wfStartTime);
            } else if(actionName.equalsIgnoreCase("stop")){
                wfEndTime = objectMapper.writeValueAsString(stepActionMap.get("endDate")).replace("\"", "");
                //System.out.println("###### Stop action:" + wfEndTime);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        //sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        java.util.Date formatedStartDate = sdf.parse(wfStartTime);
        java.util.Date formatedEndDate = formatedStartDate;//incase, there is no end time for failed WFs, the start time and end time should be same
        if(wfEndTime != null && !wfEndTime.isEmpty()){
            formatedEndDate = sdf.parse(wfEndTime);
        }

        long wfStartDateTime = formatedStartDate.getTime();
        long wfEndDateTime = formatedEndDate.getTime();
        long executionTimeSeconds = (wfEndDateTime - wfStartDateTime)/1000;

        insight.setTotalExectuionTime(executionTimeSeconds);
        insight.setWfId(wfId);
        insight.setWfName(wfName.replace("\"",""));
        insight.setStatus(wfStatus.replace("\"",""));
        long uuid = idGenerator(instanceId, wfId, logTime);
        insight.setInsightId(uuid);
        insight.setId(String.valueOf(uuid));

        //Get action and entity details

        HttpResponse<String> wfResponse = Unirest.get(url + "/bedrock-app/services/rest/workflows/" + wfId + "?projectIds=" + projectId + "&instanceId=" + instanceId + "&version=1")
                .header("cache-control", "no-cache")
                .header("Postman-Token", "5b629e06-5c9e-4d5a-91ee-ca32dfff568c")
                .asString();

        responseObject = objectMapper.readValue(wfResponse.getBody(), new TypeReference<Map<Object, Object>>(){});
        String wfDetailsResponse = objectMapper.writeValueAsString(responseObject.get("result"));
        //System.out.println("####### WF DETAILS:"+ wfDetailsResponse);
        Map<Object, Object> wfDetailsMap = objectMapper.readValue(wfDetailsResponse, new TypeReference<Map<Object, Object>>(){});
        if(wfDetailsMap == null) return null;

        String stepList = objectMapper.writeValueAsString(wfDetailsMap.get("stepList"));

        //System.out.println("####### STEP DETAILS:"+ stepList);

        List<Object> steps = objectMapper.readValue(stepList, new TypeReference<List<Object>>(){});

        List<Action> actions = new ArrayList<>();
        for(Object step : steps){
            String stepDetailString = objectMapper.writeValueAsString(step);
            Map<Object, Object> stepDetail = objectMapper.readValue(stepDetailString, new TypeReference<Map<Object, Object>>(){});
            String currentActionName = objectMapper.writeValueAsString(stepDetail.get("stepType")).replace("\"","");
            if(currentActionName.equalsIgnoreCase("null") || currentActionName.equalsIgnoreCase("start") || currentActionName.equalsIgnoreCase("stop")) continue;
            Action action = new Action();
            action.setActionName(currentActionName);
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
