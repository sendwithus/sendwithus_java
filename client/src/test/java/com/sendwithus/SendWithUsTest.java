package com.sendwithus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.Email;
import com.sendwithus.model.RenderedTemplate;
import com.sendwithus.model.SendReceipt;

@RunWith(JUnit4.class)
public class SendWithUsTest {

    public static final String SENDWITHUS_API_KEY = "THIS_IS_A_TEST_API_KEY";
    public static final String EMAIL_ID = "test_fixture_1";

    static SendWithUs sendwithusAPI;
   
    static Map<String, Object> defaultRecipientParams = new HashMap<String, Object>();
    static Map<String, Object> invalidRecipientParams = new HashMap<String, Object>();
    static Map<String, Object> defaultSenderParams = new HashMap<String, Object>();
    static Map<String, Object> defaultDataParams = new HashMap<String, Object>();
    
    @BeforeClass
    public static void setUp() {
        
        sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);

        defaultRecipientParams.put("name", "Matt");
        defaultRecipientParams.put("address", "test@sendwithus.com");

        invalidRecipientParams.put("name", "Matt");
        
        defaultSenderParams.put("name", "Company Name");
        defaultSenderParams.put("address", "company@company.com");
        defaultSenderParams.put("reply_to", "info@company.com");

        defaultDataParams.put("first_name", "Brad");
        defaultDataParams.put("link", "http://sendwithus.com/some_link");
    }

    /**
     *   Test get emails
     */
    @Test
    public void testGetEmails() throws SendWithUsException {
        
        Email[] emails = sendwithusAPI.emails();

        assertTrue(emails.length > 0);
    }

    /**
     *   Test simple send
     */
    @Test
    public void testSimpleSend() throws SendWithUsException {
        
        SendReceipt sendReceipt = sendwithusAPI.send(
            EMAIL_ID, 
            defaultRecipientParams,
            defaultDataParams
        );
        
        assertNotNull(sendReceipt);
    }

    /**
     *   Test send with sender info
     */
    @Test
    public void testSendWithSender() throws SendWithUsException {
         
        SendReceipt sendReceipt = sendwithusAPI.send(
            EMAIL_ID, 
            defaultRecipientParams,
            defaultSenderParams,
            defaultDataParams
        );
        
        assertNotNull(sendReceipt);
    }

    /**
     *   Test send with attachment
     */
    @Test
    public void testSendWithAttachment() throws SendWithUsException {

        String[] str = {"test.png", "test.png"};

        SendReceipt sendReceipt = sendwithusAPI.send(
            EMAIL_ID,
            defaultRecipientParams,
            defaultSenderParams,
            defaultDataParams,
            null,
            null,
            str
        );

        assertNotNull(sendReceipt);
    }


    /**
     *   Test send with empty attachment list
     */
    @Test
    public void testSendWithEmptyAttachment() throws SendWithUsException {

        String[] str = {};

        SendReceipt sendReceipt = sendwithusAPI.send(
            EMAIL_ID,
            defaultRecipientParams,
            defaultSenderParams,
            defaultDataParams,
            null,
            null,
            str
        );

        assertNotNull(sendReceipt);
    }


    /**
     *   Test send with incomplete receiver
     */
    @Test(expected=SendWithUsException.class)
    public void testSendIncomplete() throws SendWithUsException {
    
        SendReceipt sendReceipt = sendwithusAPI.send(
            EMAIL_ID, 
            invalidRecipientParams,
            defaultDataParams
        );
   
    }

    /**
     *   Test invalid API key
     */
    @Test(expected=SendWithUsException.class)
    public void testSendInvalidAPIKey() throws SendWithUsException {

        SendWithUs invalidAPI = new SendWithUs("INVALID_KEY");
        
        SendReceipt sendReceipt = invalidAPI.send(
            EMAIL_ID, 
            defaultRecipientParams,
            defaultDataParams
        );
    }

    /**
     *   Test invalid email_id
     */
    @Test(expected=SendWithUsException.class)
    public void testSendInvalidEmailId() throws SendWithUsException {

        SendReceipt sendReceipt = sendwithusAPI.send(
            "INVALID_EMAIL_ID", 
            defaultRecipientParams,
            defaultDataParams
        );
    }

    /**
    *   Test send Array in data params
    */
    @Test
    public void testSendArrayInData() throws SendWithUsException {

        defaultDataParams.put("array", new String[]{"send", "with", "us"});

        SendReceipt sendReceipt = sendwithusAPI.send(
            EMAIL_ID, 
            defaultRecipientParams,
            defaultDataParams
        );
        
        assertNotNull(sendReceipt);
    }

    /**
    *   Test send ArrayList in data params
    */
    @Test
    public void testSendArrayListInData() throws SendWithUsException {

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("send");
        arrayList.add("with");
        arrayList.add("us");

        defaultDataParams.put("arrayList", arrayList);

        SendReceipt sendReceipt = sendwithusAPI.send(
            EMAIL_ID, 
            defaultRecipientParams,
            defaultDataParams
        );
        
        assertNotNull(sendReceipt);
    }

    /**
     *   Test render
     */
    @Test
    public void testRender() throws SendWithUsException {
        
        RenderedTemplate renderedTemplate = sendwithusAPI.render(
            EMAIL_ID, 
            defaultDataParams
        );
        
        assertNotNull(renderedTemplate);
    }

    /**
     *   Test invalid email_id
     */
    @Test(expected=SendWithUsException.class)
    public void testRenderInvalidEmailId() throws SendWithUsException {

        RenderedTemplate renderedTemplate = sendwithusAPI.render(
            "INVALID_EMAIL_ID", 
            defaultDataParams
        );
    }

}
