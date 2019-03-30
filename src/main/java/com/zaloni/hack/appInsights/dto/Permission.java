package com.zaloni.hack.appInsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Permission {
    
    private String modulePermissionId;
    private String permissionPrettyName;
    private String permissionTechnicalName;
    private String description;
    private String permissionType;
    private String isGlobal;


    public String getModulePermissionId() {
        return modulePermissionId;
    }

    public void setModulePermissionId(String modulePermissionId) {
        this.modulePermissionId = modulePermissionId;
    }

    public String getPermissionPrettyName() {
        return permissionPrettyName;
    }

    public void setPermissionPrettyName(String permissionPrettyName) {
        this.permissionPrettyName = permissionPrettyName;
    }

    public String getPermissionTechnicalName() {
        return permissionTechnicalName;
    }

    public void setPermissionTechnicalName(String permissionTechnicalName) {
        this.permissionTechnicalName = permissionTechnicalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(String isGlobal) {
        this.isGlobal = isGlobal;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Permission.class.getSimpleName() + "[", "]")
                .add("modulePermissionId='" + modulePermissionId + "'")
                .add("permissionPrettyName='" + permissionPrettyName + "'")
                .add("permissionTechnicalName='" + permissionTechnicalName + "'")
                .add("description='" + description + "'")
                .add("permissionType='" + permissionType + "'")
                .add("isGlobal='" + isGlobal + "'")
                .toString();
    }
}
