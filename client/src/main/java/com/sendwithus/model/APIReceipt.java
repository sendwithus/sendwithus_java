package com.sendwithus.model;

/**
 * Generic response for SWU API calls yielding a receipt.
 * 
 * Unlike APIResponse, provides "success" and "status" properties.
 * 
 */
public class APIReceipt extends APIResponse {
    
    protected Boolean success; // must match JSON response
    protected String status; // must match JSON response

    public Boolean getSuccess() {
        return success;
    }

    public String getStatus() {
        return status;
    }
    
}
