package com.sendwithus.model;


public class Email extends APIResponse {
    
    private String id; // must match JSON response
    private String name; // must match JSON response

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return String.format("'%s' (ID: %s)", getName(), getID());
    }
    
}
