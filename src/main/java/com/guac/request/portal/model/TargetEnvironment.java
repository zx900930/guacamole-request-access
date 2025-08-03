package com.guac.request.portal.model;

public class TargetEnvironment {

    private String environmentId;
    private String name;
    private String description;

    public TargetEnvironment() {
    }

    public TargetEnvironment(String environmentId, String name, String description) {
        this.environmentId = environmentId;
        this.name = name;
        this.description = description;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}