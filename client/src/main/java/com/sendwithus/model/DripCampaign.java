package com.sendwithus.model;


public class DripCampaign extends APIReceipt {
    
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return String.format("DripCampaign[%s]", getId());
    }
}