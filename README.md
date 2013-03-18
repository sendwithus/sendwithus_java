# sendwithus_java

Java class for sending email via the sendwithus API.

[sendwithus.com](http://sendwithus.com)

## Installation

    $ wget https://github.com/sendwithus/sendwithus_java/raw/master/build/sendwithus-1.0.0.jar

### Dependencies
 - [junit-4.10](https://github.com/sendwithus/sendwithus_java/raw/master/lib/junit-4.10.jar)
 - [gson-2.2.2](https://github.com/sendwithus/sendwithus_java/raw/master/lib/gson-2.2.2.jar)

## Usage

### General

See [SendWithUsExample.java](https://github.com/sendwithus/sendwithus_java/blob/master/SendWithUsExample.java) for full usage.

    import java.util.HashMap;
    import java.util.Map;

    import com.sendwithus.SendWithUs;
    
    final String SENDWITHUS_API_KEY = "API-KEY-HERE";
    final String EMAIL_ID_WELCOME_EMAIL = "EMAIL-ID-HERE";
    
    SendWithUs sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);
    
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

    SendReceipt sendReceipt = sendwithusAPI.send(
        EMAIL_ID_WELCOME_EMAIL, 
        recipientMap,
        senderMap,
        emailDataMap
    );

## Errors

The following errors may be generated:

    com.sendwithus.exception.SendWithUsException - Raised by send exceptions
