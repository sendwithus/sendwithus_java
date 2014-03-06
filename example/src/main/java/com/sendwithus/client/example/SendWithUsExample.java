package com.sendwithus.client.example;

import java.util.HashMap;
import java.util.Map;

import com.sendwithus.SendWithUs;
import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.Email;
import com.sendwithus.model.SendReceipt;


public class SendWithUsExample {

    public static final String SENDWITHUS_API_KEY = "API-KEY-HERE";
    public static final String EMAIL_ID_WELCOME_EMAIL = "EMAIL-ID-HERE";

    public static void main(String[] args) {

        SendWithUs sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);

        // Print list of available emails
        try {
            Email[] emails = sendwithusAPI.emails();
            for (int i = 0; i < emails.length; i++) {
                System.out.println(emails[i].toString());
            }
        } catch (SendWithUsException e) {
            System.out.println(e.toString());
        }

        // Send Welcome Email
        Map<String, Object> recipientMap = new HashMap<String, Object>();
        recipientMap.put("name", "Matt"); // optional
        recipientMap.put("address", "us@sendwithus.com");

        // sender is optional
        Map<String, Object> senderMap = new HashMap<String, Object>();
        senderMap.put("name", "Company"); // optional
        senderMap.put("address", "company@company.com");
        senderMap.put("reply_to", "info@company.com"); // optional

        Map<String, Object> emailDataMap = new HashMap<String, Object>();
        emailDataMap.put("first_name", "Brad");
        emailDataMap.put("link", "http://sendwithus.com/some_link");

        String[] attachments = {"test.png", "test.png"};

        try {
            SendReceipt sendReceipt = sendwithusAPI.send(
                EMAIL_ID_WELCOME_EMAIL, 
                recipientMap,
                senderMap,
                emailDataMap,
                null,
                null,
                attachments
            );
            System.out.println(sendReceipt);
        } catch (SendWithUsException e) {
            System.out.println(e.toString());
        }
    }

}
