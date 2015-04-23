package com.sendwithus.model;


public class DeactivatedDrips extends APIReceipt {

    private String recipient_address; // must match JSON response
    private int unsubscribed_count; // must match JSON response

    public String getRecipient_address() {
        return recipient_address;
    }
    public int getDeactivatedDripCount() {
        return unsubscribed_count;
    }

    public String toString() {
        return String.format("DeactivatedDrips[%s]", getRecipient_address());
    }


}
