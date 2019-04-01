package com.zaloni.hack.appInsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {

    private String actionId;
    private String actionName;
    private String status;
    private int pairConnectionId;
    private String pairConnectionName;
    private int entityId;
    private String entityName;
    private int entityVersion;
    private String actionType;
    private String disabled;
    private String skipped;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPairConnectionId() {
        return pairConnectionId;
    }

    public void setPairConnectionId(int pairConnectionId) {
        this.pairConnectionId = pairConnectionId;
    }

    public String getPairConnectionName() {
        return pairConnectionName;
    }

    public void setPairConnectionName(String pairConnectionName) {
        this.pairConnectionName = pairConnectionName;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        if(entityName!=null) {
            entityName = entityName.replace("\"","");
        }
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(int entityVersion) {
        this.entityVersion = entityVersion;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getSkipped() {
        return skipped;
    }

    public void setSkipped(String skipped) {
        this.skipped = skipped;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Action.class.getSimpleName() + "[", "]")
                .add("actionId=" + actionId)
                .add("actionName='" + actionName + "'")
                .add("status='" + status + "'")
                .add("pairConnectionId=" + pairConnectionId)
                .add("pairConnectionName='" + pairConnectionName + "'")
                .add("entityId=" + entityId)
                .add("entityName='" + entityName + "'")
                .add("entityVersion=" + entityVersion)
                .add("actionType='" + actionType + "'")
                .add("disabled='" + disabled + "'")
                .add("skipped='" + skipped + "'")
                .toString();
    }
}
