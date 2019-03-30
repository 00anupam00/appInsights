package com.zaloni.hack.appInsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cluster {

    private String clusterId;
    private String clusterName;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cluster.class.getSimpleName() + "[", "]")
                .add("clusterId='" + clusterId + "'")
                .add("clusterName='" + clusterName + "'")
                .toString();
    }
}
