package com.sendwithus.model;


public class SnippetReceipt extends APIReceipt {
    
    private Snippet snippet;
    
    public Snippet getSnippet() {
        return snippet;
    }
    
    public String toString() {
        return String.format("SnippetReceipt[%s]", getSnippet().getId());
    }

}