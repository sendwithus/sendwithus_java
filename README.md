# sendwithus_java

This repository contains the source code for the  Java library for sending email via the [sendwithus](http://sendwithus.com) API.

To use the library in your own project, please follow the installation and usage instructions below - you do not need to build the library yourself.

**Warning**

This client is a working development repository, please use the `1.6.0` jar or maven repository. _Do not_ build
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
        <version>2.0.0</version>
</dependency>
```
### via wget:

    $ wget https://github.com/sendwithus/sendwithus-mvn-repo/raw/master/releases/com/sendwithus/java-client/2.0.0/java-client-2.0.0.jar

### External Dependencies (if not using maven)
 - [gson-2.2.2](https://github.com/google/gson)
 - [commons-io-2.4](http://commons.apache.org/proper/commons-io/)
 - [commons-codec](http://commons.apache.org/proper/commons-codec/)

## Usage

### Sending

See [SendWithUsExample.java](https://github.com/sendwithus/sendwithus_java/blob/master/SendWithUsExample.java) for full usage.

*NOTE* - If a customer does not exist by the specified email (recipient address), the send call will create a customer.

*NOTE* - Email ID is the ID of the Template (template id)

Two APIs now exist for issuing "Send" requests:
 - Legacy:  SendWithUs.send(...) methods

 A collection of overloaded methods for sending an Email with various parameters.  DOES NOT SUPPORT: Tags, Inline Attachments, Version Name

 - New:     SendWithUsSendRequest class

 The new method, employing SendWithUsSendRequest objects, enables a more object-oriented approach to issuing Send requests.  The objects allow you to specify only the desired request parameters, and can be re-used and mutated easily.

 Provided parameter setters:

  - setEmailId(String emailId)
  - setRecipient(Map<String, Object> recipient)
  - setEmailData(Map<String, Object> emailData)
  - setCcRecipients(Map<String, Object>[] ccRecipients)
  - setBccRecipients(Map<String, Object>[] bccRecipients)
  - setSender(Map<String, Object> sender)
  - setTags(String[] tags)
  - setInline(Map<String, String> inline)
  - setAttachmentPaths(String[] attachmentPaths)
  - setEspAccount(String espAccount)
  - setVersionName(String versionName)
  - setLocale(String locale)
  - setHeaders(Map<String, String> headers)

### Advanced

 The following methods are also available:

 - templates()
 - template(String)
 - versions(String, String)
 - render(String, Map<String, Object>)
 - deactivateDrips(String)
 - createUpdateCustomer(String, Map<String, Object>)
 - getSnippets()
 - getSnippet(String)
 - createSnippet(String, String)
 - updateSnippet(String, String, String)
 - deleteSnippet(String)

### Examples

```java
import java.util.HashMap;
import java.util.Map;

import com.sendwithus.SendWithUs;

final String SENDWITHUS_API_KEY = "API-KEY-HERE";
final String EMAIL_ID_WELCOME_EMAIL = "EMAIL-ID-HERE";
final String TEMPLATE_ID = "TEMPLATE-ID-HERE";
final String DRIP_CAMPAIGN_ID = "DRIP-CAMPAIGN-ID-HERE";
final String SNIPPET_ID = "SNIPPET-ID-HERE";

SendWithUs sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);

// Send
Map<String, Object> recipientMap = new HashMap<String, Object>();
recipientMap.put("name", "Matt"); // optional
recipientMap.put("address", "us@sendwithus.com");

// Sender is optional
Map<String, Object> senderMap = new HashMap<String, Object>();
senderMap.put("name", "Company"); // optional
senderMap.put("address", "company@company.com");
senderMap.put("reply_to", "info@company.com"); // optional

// ESP Account is optional
String espAccount = "esp_1234asdf";

// Setup Email Data for Send
Map<String, Object> emailDataMap = new HashMap<String, Object>();
emailDataMap.put("first_name", "Brad");
emailDataMap.put("link", "http://sendwithus.com/some_link");

// Attachments is optional
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

// Templates
Email template = sendwithusAPI.template(TEMPLATE_ID);
List<TemplateVersion> versions = template.getVersions();
TemplateVersion version = versions.get(0);
TemplateVersionDetails details = sendwithusAPI.version(TEMPLATE_ID, version.getId());

// Drip Campaigns
String recipientAddress = "customer@email.com";

SendWithUsDripRequest dripRequest = new SendWithUsDripRequest();
dripRequest.setRecipient(recipientMap);
dripRequest.setEmailData(emailDataMap);

ActivatedDrip activateDripCampaign = sendwithusAPI.startOnDripCampaign(DRIP_CAMPAIGN_ID, dripRequest);
DeactivatedDrip deactivateDripCampaign = sendwithusAPI.removeFromDripCampaign(recipientAddress, DRIP_CAMPAIGN_ID);
DeactivatedDrips deactivateAllDripCampaigns = sendwithusAPI.deactivateDrips(recipientAddress);

// Customers
CustomerReceipt customerReceipt = sendwithusAPI.createUpdateCustomer(recipientAddress, emailDataMap);

// Snippets
String snippetName = "My Snippet";
String snippetBody = "<h1>Snippets!</h1>";
Snippet[] snippets = sendwithusAPI.getSnippets();
Snippet snippet = sendwithusAPI.getSnippet(SNIPPET_ID);
SnippetReceipt createSnippet = sendwithusAPI.createSnippet(snippetName, snippetBody);
SnippetReceipt updateSnippet = sendwithusAPI.updateSnippet(SNIPPET_ID, snippetName, snippetBody);
APIReceipt deleteSnippet = sendwithusAPI.deleteSnippet(SNIPPET_ID);

// Render
RenderedTemplate render = sendwithusAPI.render(TEMPLATE_ID, emailDataMap);
```


## Errors

The following errors may be generated:

    com.sendwithus.exception.SendWithUsException - Raised by send exceptions

### Response Ranges

Sendwithus' API typically sends responses back in these ranges:

-   2xx – Successful Request
-   4xx – Failed Request (Client error)
-   5xx – Failed Request (Server error)

If you're receiving an error in the 400 response range follow these steps:

-   Double check the data and ID's getting passed to sendwithus
-   Ensure your API key is correct
-   Log and check the body of the response
