package com.sendwithus.model;


public class ActivatedDrip extends APIReceipt {

    private DripCampaign drip_campaign; // must match JSON response

    private String recipient_address; // must match JSON response
    private String locale; // must match JSON response
    private String message; // must match JSON response

    public DripCampaign getDrip_campaign() {
        return drip_campaign;
    }

    public String getRecipient_address() {
        return recipient_address;
    }

    public String getLocale() {
        return locale;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return String.format("DeactivatedDrip[%s]", getRecipient_address());
    }
}
