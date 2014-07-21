package com.sendwithus.model;


public class SendReceipt extends APIReceipt {
    
    private String receipt_id; // must match JSON response

    public String getReceiptID() {
        return receipt_id;
    }

    public String toString() {
        return String.format("SendReceipt[%s]", getReceiptID());
    }
    
}
