import java.util.HashMap;
import java.util.Map;

import com.sendwithus.SendWithUs;
import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.DeactivatedDrips;
import com.sendwithus.model.Email;
import com.sendwithus.model.SendReceipt;


public class SendWithUsExample {

    public static final String SENDWITHUS_API_KEY = "YOUR-API-KEY-HERE";
    public static final String EMAIL_ID_WELCOME_EMAIL = "YOUR-EMAIL-ID-HERE";

    public static void main(String[] args) {

        SendWithUs sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);

        // Print list of available emails
        try {
            Email[] emails = sendwithusAPI.templates();
            for (int i = 0; i < emails.length; i++) {
                System.out.println(emails[i].toString());
            }
        } catch (SendWithUsException e) {
            System.out.println(e.toString());
        }

        // Send Welcome Email
        Map<String, Object> recipientMap = new HashMap<String, Object>();
        recipientMap.put("name", "Brad"); // optional
        recipientMap.put("address", "brad@sendwithus.com");

        // sender is optional
        Map<String, Object> senderMap = new HashMap<String, Object>();
        senderMap.put("name", "Company"); // optional
        senderMap.put("address", "java@sendwithus.com");
        senderMap.put("reply_to", "java@sendwithus.com"); // optional

        // email data in to inject in the email template
        Map<String, Object> emailDataMap = new HashMap<String, Object>();
        emailDataMap.put("first_name", "Brad");
        emailDataMap.put("link", "http://sendwithus.com/some_link");

        // Example sending a simple email
        try {
            SendReceipt sendReceipt = sendwithusAPI.send(
                EMAIL_ID_WELCOME_EMAIL,
                recipientMap,
                senderMap,
                emailDataMap
            );
            System.out.println(sendReceipt);
        } catch (SendWithUsException e) {
            System.out.println(e.toString());
        }
        
        // Example sending the same simple email using the new SendWithUsSendRequest class
        try {
            SendWithUsSendRequest request = new SendWithUsSendRequest()
            	.setEmailId(EMAIL_ID_WELCOME_EMAIL)
        		.setRecipient(recipientMap)
        		.setSender(senderMap)
        		.setEmailData(emailDataMap);
            SendReceipt sendReceipt = sendwithusAPI.send(request);
            System.out.println(sendReceipt);
        } catch (SendWithUsException e) {
            System.out.println(e.toString());
        }
    }

}
