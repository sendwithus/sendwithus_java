package com.sendwithus.model;

public class Snippet {

    private String id; // must match JSON response
    private String name; // must match JSON response
    private String body; // must match JSON response
    private Long created; // must match JSON response
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getBody() {
        return body;
    }
    
    public Long getCreated() {
        return created;
    }
    
    public String toString() {
        return String.format("Snippet[%s]", getId());
    }

}