package com.sendwithus.model;


public class DeactivatedDrips extends APIReceipt {

    private String recipient_address; // must match JSON response

    public String getRecipientAddress() {
        return recipient_address;
    }

    public String toString() {
        return String.format("DeactivatedDrips[%s]", getRecipientAddress());
    }

}
