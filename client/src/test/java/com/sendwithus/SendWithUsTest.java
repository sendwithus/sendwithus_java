package com.sendwithus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sendwithus.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.sendwithus.exception.SendWithUsException;

@RunWith(JUnit4.class)
public class SendWithUsTest
{

    public static final String SENDWITHUS_API_KEY = "JAVA_API_CLIENT_TEST_KEY";
    public static final String EMAIL_ID = "test_fixture_1";
    public static final String TEMPLATE_ID = "test_template_1";
    public static final String VERSION_ID = "test_version_1";

    SendWithUs sendwithusAPI;

    Map<String, Object> defaultRecipientParams = new HashMap<String, Object>();
    Map<String, Object> invalidRecipientParams = new HashMap<String, Object>();
    Map<String, Object> defaultSenderParams = new HashMap<String, Object>();
    Map<String, Object> defaultDataParams = new HashMap<String, Object>();
    
    private static final String TEST_RECIPIENT_ADDRESS = "swunit+javaclient@sendwithus.com";

    @Before
    public void setUp()
    {
        sendwithusAPI = Mockito.spy(new SendWithUs(SENDWITHUS_API_KEY));

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
        Mockito.doReturn(
                "[{'id': 'test-snippet-id', 'name': 'test-snippet', 'body': '<h1>test header</h1>', 'created': 1414091325}]"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("templates"), Mockito.eq("GET")
        );

        Email[] emails = sendwithusAPI.templates();

        assertTrue(emails.length > 0);
        for (Email emailReceipt : emails) {
            assertSuccessfulAPIResponse(emailReceipt);
        }
    }

    /**
     * Test get template
     */
    @Test
    public void testGetTemplate() throws SendWithUsException
    {
        Mockito.doReturn(
                "{'id': '" + TEMPLATE_ID + "', 'name': 'test-template'," +
                        "    'created': 1411606421," +
                        "    'locale': 'en-US'," +
                        "    'versions': [" +
                        "        {" +
                        "            'name': 'Version Name'," +
                        "            'id': 'ver_arstneiotsra'," +
                        "            'created': '1411606421'," +
                        "            'modified': '1411606421'" +
                        "        }" +
                        "    ]," +
                        "    'tags': ['tag1', 'tag2']}"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("templates/" + TEMPLATE_ID), Mockito.eq("GET")
        );
        Email email = sendwithusAPI.template(TEMPLATE_ID);
        assertSuccessfulAPIResponse(email);
        assertEquals("test-template", email.getName());
    }

    /**
     * Test get template with locale
     */
    @Test
    public void testGetTemplateWithLocale() throws SendWithUsException
    {
        Mockito.doReturn(
                "{'id': '" + TEMPLATE_ID + "', 'name': 'test-template'," +
                        "    'created': 1411606421," +
                        "    'locale': 'en-CA'," +
                        "    'versions': [" +
                        "        {" +
                        "            'name': 'Version Name'," +
                        "            'id': 'ver_arstneiotsra'," +
                        "            'created': '1411606421'," +
                        "            'modified': '1411606421'" +
                        "        }" +
                        "    ]," +
                        "    'tags': ['tag1', 'tag2']}"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("templates/" + TEMPLATE_ID + "/locales/en-CA"), Mockito.eq("GET")
        );
        Email email = sendwithusAPI.template(TEMPLATE_ID, "en-CA");
        assertSuccessfulAPIResponse(email);
        assertEquals("test-template", email.getName());
    }

    /**
     * Test get template version
     */
    @Test
    public void testGetTemplateVersion() throws SendWithUsException
    {
        Mockito.doReturn(
                "{'id': 'test-templateVersion-id', 'name': 'test-version', 'html': '<h1>test header</h1>', 'created': 1414091325}"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("templates/" + TEMPLATE_ID + "/versions/" + VERSION_ID), Mockito.eq("GET")
        );
        TemplateVersionDetails version = sendwithusAPI.templateVersion(TEMPLATE_ID, VERSION_ID);
        assertSuccessfulAPIResponse(version);
        assertEquals("<h1>test header</h1>", version.getHtml());
    }

    /**
     * Test get template version
     */
    @Test
    public void testGetTemplateVersionWithLocale() throws SendWithUsException
    {
        Mockito.doReturn(
                "{'id': 'test-templateVersion-id', 'name': 'test-version', 'html': '<h1>test header</h1>', 'created': 1414091325}"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("templates/" + TEMPLATE_ID + "/locales/en-CA/versions/" + VERSION_ID), Mockito.eq("GET")
        );
        TemplateVersionDetails version = sendwithusAPI.templateVersion(TEMPLATE_ID, VERSION_ID, "en-CA");
        assertSuccessfulAPIResponse(version);
        assertEquals("<h1>test header</h1>", version.getHtml());
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
        assertNotNull(sendReceipt.getEmail());
        assertEquals(sendReceipt.getEmail().getLocale(), "en-US");
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
     * Test send with non-empty locale
     */
    @Test
    public void testSendWithLocale() throws SendWithUsException
    {
        String locale = "en-US";

        SendWithUsSendRequest request = new SendWithUsSendRequest()
                .setEmailId(EMAIL_ID).setRecipient(defaultRecipientParams)
                .setSender(defaultSenderParams).setEmailData(defaultDataParams)
                .setLocale(locale);

        SendReceipt sendReceipt = sendwithusAPI.send(request);

        assertSuccessfulAPIReceipt(sendReceipt);
    }

    /**
     * Test send with headers
     */
    @Test
    public void testSendWithHeaders() throws SendWithUsException
    {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Auto-Response-Suppress", "header-value");

        SendWithUsSendRequest request = new SendWithUsSendRequest()
                .setEmailId(EMAIL_ID).setRecipient(defaultRecipientParams)
                .setSender(defaultSenderParams).setEmailData(defaultDataParams)
                .setHeaders(headers);

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
        SendWithUs invalidAPI = new SendWithUs("SWUTEST_INVALID_KEY");

        invalidAPI.send(EMAIL_ID, defaultRecipientParams, defaultDataParams);
    }

    /**
     * Test invalid email_id
     */
    @Test(expected = SendWithUsException.class)
    public void testSendInvalidEmailId() throws SendWithUsException
    {
        sendwithusAPI.send("SWUTEST_INVALID_EMAIL_ID", defaultRecipientParams,
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
        sendwithusAPI.render("SWUTEST_INVALID_EMAIL_ID", defaultDataParams);
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
    
    /**
     * Test getting all snippets
     */
    @Test
    public void testGetSnippets() throws Exception
    {
        Mockito.doReturn(
                "[{'id': 'test-snippet-id', 'name': 'test-snippet', 'body': '<h1>test header</h1>', 'created': 1414091325}]"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("snippets"), Mockito.eq("GET")
        );
        
        Snippet[] snippets = sendwithusAPI.getSnippets();

        assertNotNull(snippets);
        assertTrue(snippets.length > 0);
        assertNotNull(snippets[0]);
        assertEquals("test-snippet", snippets[0].getName());
    }
    
    /**
     * Test get specific snippet
     */
    @Test
    public void testGetSnippet() throws Exception
    {
        String snippetId = "test-snippet-id";
        
        Mockito.doReturn(
                "{'id': 'test-snippet-id', 'name': 'test-snippet', 'body': '<h1>test header</h1>', 'created': 1414091325}"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("snippets/"+snippetId), Mockito.eq("GET")
        );

        Snippet snippet = sendwithusAPI.getSnippet(snippetId);

        assertNotNull(snippet);
        assertTrue(snippet instanceof Snippet);
    }
    
    /**
     * Test creating a new snippet
     */
    @Test
    public void testCreateSnippet() throws Exception
    {
        Mockito.doReturn(
                "{'success': true, 'status': 'OK'}"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("snippets"), Mockito.eq("POST"), Mockito.anyMap()
        );
        
        String name = "test-snippet";
        String body = "<h1>test header</h1>";

        SnippetReceipt receipt = sendwithusAPI.createSnippet(name, body);

        assertSuccessfulAPIReceipt(receipt);
        assertTrue(receipt instanceof SnippetReceipt);
    }
    
    /**
     * Test updating a snippet
     */
    @Test
    public void testUpdateSnippet() throws Exception
    {
        String snippetId = "test-snippet-id";
        
        Mockito.doReturn(
                "{'success': true, 'status': 'OK'}"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("snippets/"+snippetId), Mockito.eq("PUT"), Mockito.anyMap()
        );
        
        String name = "test-snippet";
        String body = "<h1>test header</h1>";

        SnippetReceipt receipt = sendwithusAPI.updateSnippet(snippetId, name, body);

        assertSuccessfulAPIReceipt(receipt);
        assertTrue(receipt instanceof SnippetReceipt);
    }
    
    /**
     * Test deleting a snippet
     */
    @Test
    public void testDeleteSnippet() throws Exception
    {
        String snippetId = "test-snippet-id";
        
        Mockito.doReturn(
                "{'success': true, 'status': 'OK'}"
        ).when(sendwithusAPI).makeURLRequest(
                Mockito.endsWith("snippets/"+snippetId), Mockito.eq("DELETE")
        );

        APIReceipt receipt = sendwithusAPI.deleteSnippet(snippetId);

        assertSuccessfulAPIReceipt(receipt);
        assertTrue(receipt instanceof APIReceipt);
    }


    /**
     * Test deactivate all drips for a customer
     */
    @Test
    public void testDeactivateAllDrips() throws Exception
    {
        DeactivatedDrips receipt = sendwithusAPI.deactivateDrips(TEST_RECIPIENT_ADDRESS);
        assertSuccessfulAPIResponse(receipt);
    }

    /**
     * Test default api location.
     */
    @Test
    public void testDefaultApiLocation() throws Exception
    {
        sendwithusAPI.templates();

        String url = String.format("%s://%s:%s/api/v%s/%s", SendWithUsApiLocation.PROTO, SendWithUsApiLocation.HOST, SendWithUsApiLocation.PORT, SendWithUsApiLocation.VERSION, "templates");
        Mockito.verify(sendwithusAPI).makeURLRequest(url, "GET", null);
    }

    /**
     * Test custom api location.
     */
    @Test
    public void testCustomApiLocation() throws Exception
    {
        String apiLocation = "http://localhost:8888/v1";
        SendWithUs sendwithusAPI = Mockito.spy(new SendWithUs(SENDWITHUS_API_KEY, new SendWithUsApiLocation(apiLocation)));
        String url = String.format("%s/%s", apiLocation, "templates");
        Mockito.doReturn("[]").when(sendwithusAPI).makeURLRequest(url, "GET", null);

        sendwithusAPI.templates();

        Mockito.verify(sendwithusAPI).makeURLRequest(url, "GET", null);
    }
}
