package com.sendwithus.model;


public class SendReceipt {
    private String receipt_id; // must match JSON response

    public String getReceiptID() {
        return this.receipt_id;
    }

    public String toString() {
        return String.format("SendReceipt[%s]", this.receipt_id);
    }
}
