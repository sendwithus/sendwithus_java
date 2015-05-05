package com.sendwithus.model;


public class DeactivatedDrip extends APIReceipt {

    private DripCampaign drip_campaign; // must match JSON response

    private String recipient_address; // must match JSON response
    private String message; // must match JSON response

    public DripCampaign getDripCampaign() {
        return drip_campaign;
    }

    public String getRecipientAddress() {
        return recipient_address;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return String.format("DeactivatedDrip[%s]", getRecipientAddress());
    }
}
