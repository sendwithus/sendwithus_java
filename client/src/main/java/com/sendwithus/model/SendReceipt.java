package com.sendwithus.model;


public class SendReceipt extends APIReceipt {
    
    private String receipt_id; // must match JSON response
    private EmailSendDetails email; // must match JSON response

    public String getReceiptID() {
        return receipt_id;
    }

    public EmailSendDetails getEmail() {
        return email;
    }

    public String toString() {
        return String.format("SendReceipt[%s]", getReceiptID());
    }

    public class EmailSendDetails {
        private String name; // must match JSON response
        private String version_name; // must match JSON response
        private String locale; // must match JSON response
    
        public String getName() {
            return name;
        }
    
        public String getVersionName() {
            return version_name;
        }
    
        public String getLocale() {
            return locale;
        }
    
        public String toString() {
            return String.format("EmailSendDetails[%s]", getName());
        }
        
    }
    
}
