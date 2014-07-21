package com.sendwithus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.APIReceipt;
import com.sendwithus.model.APIResponse;
import com.sendwithus.model.CustomerReceipt;
import com.sendwithus.model.Email;
import com.sendwithus.model.RenderedTemplate;
import com.sendwithus.model.SendReceipt;

@RunWith(JUnit4.class)
public class SendWithUsTest
{

    public static final String SENDWITHUS_API_KEY = "THIS_IS_A_TEST_API_KEY";
    public static final String EMAIL_ID = "test_fixture_1";

    static SendWithUs sendwithusAPI;

    static Map<String, Object> defaultRecipientParams = new HashMap<String, Object>();
    static Map<String, Object> invalidRecipientParams = new HashMap<String, Object>();
    static Map<String, Object> defaultSenderParams = new HashMap<String, Object>();
    static Map<String, Object> defaultDataParams = new HashMap<String, Object>();
    
    private static final String TEST_RECIPIENT_ADDRESS = "swunit+javaclient@sendwithus.com";

    @BeforeClass
    public static void setUp()
    {

        sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);

        defaultRecipientParams.put("name", "Unit Tests - Java");
        defaultRecipientParams.put("address", TEST_RECIPIENT_ADDRESS);

        invalidRecipientParams.put("name", "Unit Tests - Java");

        defaultSenderParams.put("name", "Company Name");
        defaultSenderParams.put("address", "company@example.com");
        defaultSenderParams.put("reply_to", "info@example.com");

        defaultDataParams.put("first_name", "Java Client");
        defaultDataParams.put("link", "http://sendwithus.com/some_link");
    }
    
    private static void assertSuccessfulAPIReceipt(APIReceipt receipt) {
        assertNotNull(receipt);
        assertEquals("OK", receipt.getStatus());
        assertEquals(true, receipt.getSuccess());
    }
    
    private static void assertSuccessfulAPIResponse(APIResponse response) {
        assertNotNull(response);
    }

    /**
     * Test get emails
     */
    @Test
    public void testGetEmails() throws SendWithUsException
    {
        Email[] emails = sendwithusAPI.emails();

        assertTrue(emails.length > 0);
        for (Email emailReceipt : emails) {
            assertSuccessfulAPIResponse(emailReceipt);
        }
    }

    /**
     * Test get templates
     */
    @Test
    public void testGetTemplates() throws SendWithUsException
    {
        Email[] emails = sendwithusAPI.templates();

        assertTrue(emails.length > 0);
        for (Email emailReceipt : emails) {
            assertSuccessfulAPIResponse(emailReceipt);
        }
    }

    /**
     * Test simple send
     */
    @Test
    public void testSimpleSend() throws SendWithUsException
    {
        SendReceipt sendReceipt = sendwithusAPI.send(EMAIL_ID,
                defaultRecipientParams, defaultDataParams);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send with sender info
     */
    @Test
    public void testSendWithSender() throws SendWithUsException
    {
        SendReceipt sendReceipt = sendwithusAPI.send(EMAIL_ID,
                defaultRecipientParams, defaultSenderParams, defaultDataParams);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send with attachment
     */
    @Test
    public void testSendWithAttachment() throws SendWithUsException
    {
        String[] str = { "test.png", "test.png" };

        SendReceipt sendReceipt = sendwithusAPI.send(EMAIL_ID,
                defaultRecipientParams, defaultSenderParams, defaultDataParams,
                null, null, str);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send with empty attachment list
     */
    @Test
    public void testSendWithEmptyAttachment() throws SendWithUsException
    {
        String[] str = {};

        SendReceipt sendReceipt = sendwithusAPI.send(EMAIL_ID,
                defaultRecipientParams, defaultSenderParams, defaultDataParams,
                null, null, str);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send with empty esp account
     */
    @Test
    public void testSendWithEspAccount() throws SendWithUsException
    {
        String espAccount = "";

        SendReceipt sendReceipt = sendwithusAPI.send(EMAIL_ID,
                defaultRecipientParams, defaultSenderParams, defaultDataParams,
                null, null, null, espAccount);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send with empty esp account
     */
    @Test
    public void testSendWithVersionName() throws SendWithUsException
    {
        String versionName = "";

        SendWithUsSendRequest request = new SendWithUsSendRequest()
                .setEmailId(EMAIL_ID).setRecipient(defaultRecipientParams)
                .setSender(defaultSenderParams).setEmailData(defaultDataParams)
                .setVersionName(versionName);

        SendReceipt sendReceipt = sendwithusAPI.send(request);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send with default request object
     */
    @Test
    public void testSendWithSWURequestObject() throws SendWithUsException
    {
        String espAccount = "";

        SendWithUsSendRequest request = new SendWithUsSendRequest()
                .setEmailId(EMAIL_ID).setRecipient(defaultRecipientParams)
                .setSender(defaultSenderParams).setEmailData(defaultDataParams)
                .setEspAccount(espAccount);

        SendReceipt sendReceipt = sendwithusAPI.send(request);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send with incomplete receiver
     */
    @Test(expected = SendWithUsException.class)
    public void testSendIncomplete() throws SendWithUsException
    {
        sendwithusAPI.send(EMAIL_ID, invalidRecipientParams, defaultDataParams);

    }

    /**
     * Test invalid API key
     */
    @Test(expected = SendWithUsException.class)
    public void testSendInvalidAPIKey() throws SendWithUsException
    {
        SendWithUs invalidAPI = new SendWithUs("INVALID_KEY");

        invalidAPI.send(EMAIL_ID, defaultRecipientParams, defaultDataParams);
    }

    /**
     * Test invalid email_id
     */
    @Test(expected = SendWithUsException.class)
    public void testSendInvalidEmailId() throws SendWithUsException
    {
        sendwithusAPI.send("INVALID_EMAIL_ID", defaultRecipientParams,
                defaultDataParams);
    }

    /**
     * Test send Array in data params
     */
    @Test
    public void testSendArrayInData() throws SendWithUsException
    {
        defaultDataParams.put("array", new String[] { "send", "with", "us" });

        SendReceipt sendReceipt = sendwithusAPI.send(EMAIL_ID,
                defaultRecipientParams, defaultDataParams);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send ArrayList in data params
     */
    @Test
    public void testSendArrayListInData() throws SendWithUsException
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("send");
        arrayList.add("with");
        arrayList.add("us");

        defaultDataParams.put("arrayList", arrayList);

        SendReceipt sendReceipt = sendwithusAPI.send(EMAIL_ID,
                defaultRecipientParams, defaultDataParams);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test render
     */
    @Test
    public void testRender() throws SendWithUsException
    {
        RenderedTemplate renderedTemplate = sendwithusAPI.render(EMAIL_ID,
                defaultDataParams);

        assertSuccessfulAPIReceipt(renderedTemplate);
    }

    /**
     * Test invalid email_id
     */
    @Test(expected = SendWithUsException.class)
    public void testRenderInvalidEmailId() throws SendWithUsException
    {
        sendwithusAPI.render("INVALID_EMAIL_ID", defaultDataParams);
    }

    /**
     * Test Create/Update customer
     */
    @Test
    public void testCreateUpdateCustomer() throws SendWithUsException
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("first_name", "Matt");

        CustomerReceipt receipt = sendwithusAPI.createUpdateCustomer(
                TEST_RECIPIENT_ADDRESS,
                params);

        assertSuccessfulAPIReceipt(receipt);
    }

}
