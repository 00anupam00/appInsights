package com.zaloni.hack.appInsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String userId;
    private String userName;
    private List<Permission> userPermission;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Permission> getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(List<Permission> userPermission) {
        this.userPermission = userPermission;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("userId='" + userId + "'")
                .add("userName='" + userName + "'")
                .add("userPermission=" + userPermission)
                .toString();
    }
}
