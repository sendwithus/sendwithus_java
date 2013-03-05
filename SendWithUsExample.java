import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sendwithus.SendWithUs;
import com.sendwithus.exception.SendWithUsException;


public class SendWithUsExample {

    public static final String EMAIL_ID_WELCOME_EMAIL = "<EMAIL ID HERE>";

    public static void main(String[] args) {

        SendWithUs sendwithusApi = new SendWithUs("<YOUR API KEY HERE>");

        Map<String, Object> emailDataMap = new HashMap<String, Object>();
        emailDataMap.put("first_name", "Brad");
        emailDataMap.put("link", "http://sendwithus.com/some_link");

        try {
            sendwithusApi.send(
                EMAIL_ID_WELCOME_EMAIL, 
                "java.example@sendwithus.com",
                emailDataMap
            );    
        } catch (SendWithUsException e) {
            System.out.println(e.toString());
        }
    }
    
}
