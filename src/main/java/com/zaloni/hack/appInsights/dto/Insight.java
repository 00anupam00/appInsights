package com.zaloni.hack.appInsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.StringJoiner;

@Document(indexName = "testindex", type = "app_insights")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Insight {

    //TODO: We must update not add to the index based on instanceID
    @Id
    private String id;

    @Field
    private long insightId;

    @Field(type = FieldType.Text)
    private ZdpFeature zdpFeature= ZdpFeature.WORKFLOW;
    @Field
    private int instanceId;
    @Field
    private int wfId;
    @Field
    private String wfName;
    @Field
    private int projectId;
    @Field
    private String logTime;
    @Field
    private String status;
    @Field
    private String startTime;
    @Field
    private String endTime;
    @Field
    private long totalExectuionTime;
    @Field
    private boolean impersonation;
    @Field
    private String impersonatedUser;

    private String executedBy;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Action> actions;
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Cluster> cluster;

    public long getInsightId() {
        return insightId;
    }

    public void setInsightId(long insightId) {
        this.insightId = insightId;
    }

    public ZdpFeature getZdpFeature() {
        return zdpFeature;
    }

    public void setZdpFeature(ZdpFeature zdpFeature) {
        this.zdpFeature = zdpFeature;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public int getWfId() {
        return wfId;
    }

    public void setWfId(int wfId) {
        this.wfId = wfId;
    }

    public String getWfName() {
        return wfName;
    }

    public void setWfName(String wfName) {
        this.wfName = wfName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getTotalExectuionTime() {
        return totalExectuionTime;
    }

    public void setTotalExectuionTime(long totalExectuionTime) {
        this.totalExectuionTime = totalExectuionTime;
    }

    public boolean isImpersonation() {
        return impersonation;
    }

    public void setImpersonation(boolean impersonation) {
        this.impersonation = impersonation;
    }

    public String getImpersonatedUser() {
        return impersonatedUser;
    }

    public void setImpersonatedUser(String impersonatedUser) {
        this.impersonatedUser = impersonatedUser;
    }

    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Cluster> getCluster() {
        return cluster;
    }

    public void setCluster(List<Cluster> cluster) {
        this.cluster = cluster;
    }

    public String getId() {
        return id;
    }

    public Insight setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Insight.class.getSimpleName() + "[", "]")
                .add("insightId='" + insightId + "'")
                .add("zdpFeature=" + zdpFeature)
                .add("instanceId=" + instanceId)
                .add("wfId=" + wfId)
                .add("wfName='" + wfName + "'")
                .add("projectId=" + projectId)
                .add("logTime='" + logTime + "'")
                .add("status='" + status + "'")
                .add("startTime='" + startTime + "'")
                .add("endTime='" + endTime + "'")
                .add("totalExectuionTime='" + totalExectuionTime + "'")
                .add("impersonation=" + impersonation)
                .add("impersonatedUser='" + impersonatedUser + "'")
                .add("executedBy='" + executedBy + "'")
                .add("actions=" + actions)
                .add("cluster=" + cluster)
                .toString();
    }
}
