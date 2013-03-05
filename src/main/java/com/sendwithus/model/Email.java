package com.sendwithus.model;


public class Email {
    private String id;
    private String name;

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return String.format("'%s' (ID: %s)", this.name, this.id);
    }
}
