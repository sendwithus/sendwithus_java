package com.sendwithus.model;


import java.util.List;

public class Email extends APIResponse {
    
    private String id; // must match JSON response
    private String name; // must match JSON response
    private List<TemplateVersion> versions;

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return String.format("'%s' (ID: %s)", getName(), getID());
    }

    public List<TemplateVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<TemplateVersion> versions) {
        this.versions = versions;
    }
}
