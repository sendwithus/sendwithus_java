package com.sendwithus.model;
import java.util.Map;


public class CustomerReceipt {

    Boolean     success;
    String      status;
    Customer customer;

    public static class Customer {
        String      email;
        long        created;

        public String getEmail() {
            return this.email;
        }

        public long getCreated() {
            return this.created;
        }
    }

    public String getEmail() {
        return this.customer.getEmail();
    }

    public String toString() {
        return String.format("CustomerReceipt[%s]", this.customer.getEmail());
    }
}
