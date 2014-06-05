# sendwithus_java

This repository contains the source code for the  Java library for sending email via the [sendwithus](http://sendwithus.com) API.

To use the library in your own project, please follow the installation and usage instructions below - you do not need to build the library yourself.

**Warning**

This client is a working development repository, please use the `1.1.0` jar or maven repository. _Do not_ build
the client yourself.

## Installation

### via maven (preferred)

Add the following to your pom.xml:

```xml
<!-- in the repositories section -->
<repository>
        <id>repo</id>
        <url>https://github.com/sendwithus/sendwithus-mvn-repo/raw/master/releases</url>
</repository>

<!-- in the dependencies section -->
<dependency>
        <groupId>com.sendwithus</groupId>
        <artifactId>java-client</artifactId>
        <version>1.1.0</version>
</dependency>
```
### via wget:

    $ wget https://github.com/sendwithus/sendwithus-mvn-repo/raw/master/releases/com/sendwithus/java-client/1.1.0/java-client-1.1.0.jar

### External Dependencies (if not using maven)
 - [gson-2.2.2](http://google-gson.googlecode.com/files/google-gson-2.2.2-release.zip)
 - [commons-io-2.4](http://commons.apache.org/proper/commons-io/)
 - [commons-codec](http://commons.apache.org/proper/commons-codec/)

## Usage

### General

See [SendWithUsExample.java](https://github.com/sendwithus/sendwithus_java/blob/master/example/src/main/java/com/sendwithus/client/example/SendWithUsExample.java) for full usage.

Two APIs now exist for issuing "Send" requests:
 - Legacy:  SendWithUs.send(...) methods

 A collection of overloaded methods for sending an Email with various parameters.  DOES NOT SUPPORT: Tags, Inline Attachments

 - New:     SendWithUsSendRequest class

 The new method, employing SendWithUsSendRequest objects, enables a more object-oriented approach to issuing Send requests.  The objects allow you to specify only the desired request parameters, and can be re-used and mutated easily.

### Examples

```java
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

// ESP account is an advanced feature, optional
String espAccount = "tdf_123981";

Map<String, Object> emailDataMap = new HashMap<String, Object>();
emailDataMap.put("first_name", "Brad");
emailDataMap.put("link", "http://sendwithus.com/some_link");

String[] attachments = {"test.png"}

// Sending the email using the legacy send() java API
SendReceipt sendReceipt = sendwithusAPI.send(
    EMAIL_ID_WELCOME_EMAIL,
    recipientMap,
    senderMap,
    emailDataMap,
    null,
    null,
    attachments,
    espAccount
);

// Example sending the same email using the new SendWithUsSendRequest class
SendWithUsSendRequest request = new SendWithUsSendRequest()
    .setEmailId(EMAIL_ID_WELCOME_EMAIL)
    .setRecipient(recipientMap)
    .setSender(senderMap)
    .setEmailData(emailDataMap)
    .setAttachmentPaths(attachments)
    .setEspAccount(espAccount);
SendReceipt sendReceipt = sendwithusAPI.send(request);
```


## Errors

The following errors may be generated:

    com.sendwithus.exception.SendWithUsException - Raised by send exceptions
