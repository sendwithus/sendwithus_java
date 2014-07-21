package com.sendwithus.model;


public class CustomerReceipt extends APIReceipt {

    private Customer customer; // must match JSON response

    public static class Customer {
        private String email; // must match JSON response
        private long created; // must match JSON response

        public String getEmail() {
            return email;
        }

        public long getCreated() {
            return created;
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCustomerEmail() {
        return customer.getEmail();
    }

    public String toString() {
        return String.format("CustomerReceipt[%s]", getCustomer().getEmail());
    }
    
}
