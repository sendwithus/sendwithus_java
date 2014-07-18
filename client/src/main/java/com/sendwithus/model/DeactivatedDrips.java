package com.sendwithus.model;


public class DeactivatedDrips {

    Boolean     success;
    String      status;
    String      email_address;
    int         unsubscribed_count;

    public Boolean getSuccess() {
        return this.success;
    }

    public String getStatus() {
        return this.status;
    }

    public String getEmailAddress() {
        return this.email_address;
    }

    public int getDeactivatedDripCount() {
        return this.unsubscribed_count;
    }
}
