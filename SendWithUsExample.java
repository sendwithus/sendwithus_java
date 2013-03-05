import java.io.IOException;
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
        Map<String, Object> emailDataMap = new HashMap<String, Object>();
        emailDataMap.put("first_name", "Brad");
        emailDataMap.put("link", "http://sendwithus.com/some_link");

        try {
            SendReceipt sendReceipt = sendwithusAPI.send(
                EMAIL_ID_WELCOME_EMAIL, 
                "us@sendwithus.com",
                emailDataMap
            );
            System.out.println(sendReceipt);
        } catch (SendWithUsException e) {
            System.out.println(e.toString());
        }
    }
    
}
