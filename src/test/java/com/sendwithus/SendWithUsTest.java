import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import com.sendwithus.SendWithUs;
import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.Email;
import com.sendwithus.model.SendReceipt;

public class SendWithUsTest {

    public static final String SENDWITHUS_API_KEY = "API-KEY-HERE";
    public static final String EMAIL_ID_WELCOME_EMAIL = "EMAIL-ID-HERE";
    
    static Map<String, Object> defaultRecipientParams = new HashMap<String, Object>();
    static Map<String, Object> defaultSenderParams = new HashMap<String, Object>();
    static Map<String, Object> defaultDataParams = new HashMap<String, Object>();
    
    @BeforeClass
    public static void setUp() {
        
        defaultRecipientParams.put("name", "Matt");
        defaultRecipientParams.put("address", "us@sendwithus.com");

        defaultSenderParams.put("name", "Company Name");
        defaultSenderParams.put("address", "company@company.com");
        defaultSenderParams.put("reply_to", "info@company.com");

        defaultDataParams.put("first_name", "Brad");
        defaultDataParams.put("link", "http://sendwithus.com/some_link");
    }

    @Test
    public void testGetEmails() throws SendWithUsException {
        
        SendWithUs sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);
    
        Email[] emails = sendwithusAPI.emails();

        assertTrue(emails.length > 0);

        System.out.println("successfuly got emails");
    }
}
