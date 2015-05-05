package com.sendwithus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.APIReceipt;
import com.sendwithus.model.ActivatedDrip;
import com.sendwithus.model.CustomerReceipt;
import com.sendwithus.model.DeactivatedDrip;
import com.sendwithus.model.DeactivatedDrips;
import com.sendwithus.model.Email;
import com.sendwithus.model.RenderedTemplate;
import com.sendwithus.model.SendReceipt;
import com.sendwithus.model.Snippet;
import com.sendwithus.model.SnippetReceipt;

/**
 * SendWithUs API interface.
 * 
 * Reference: https://github.com/sendwithus/sendwithus_java
 */
public class SendWithUs
{
    
    public static final String API_PROTO = "https";
    public static final String API_HOST = "api.sendwithus.com";
    public static final String API_PORT = "443";
    
    public static final String API_VERSION = "1";
    public static final String CLIENT_VERSION = "1.6.0";
    public static final String CLIENT_LANG = "java";
    public static final String SWU_API_HEADER = "X-SWU-API-KEY";
    public static final String SWU_CLIENT_HEADER = "X-SWU-API-CLIENT";

    private String apiKey;

    public SendWithUs(String apiKey)
    {
        this.apiKey = apiKey;
    }

    private String getURLEndpoint(String resourceName)
    {
        return String.format("%s://%s:%s/api/v%s/%s", SendWithUs.API_PROTO,
                SendWithUs.API_HOST, SendWithUs.API_PORT,
                SendWithUs.API_VERSION, resourceName);
    }

    private HttpURLConnection createConnection(
            String url, String method, Map<String, Object> params)
            throws IOException
    {
        URL connectionURL = new URL(url);
        HttpURLConnection connection;
        
        /* Detect and allow HTTP connections (useful for testing) */
        if (connectionURL.getProtocol().equals("https")) {
            connection = (HttpsURLConnection) connectionURL.openConnection();
        } else {
            connection = (HttpURLConnection) connectionURL.openConnection();
        }
        
        connection.setConnectTimeout(30000); // 30 seconds
        connection.setReadTimeout(60000); // 60 seconds
        connection.setUseCaches(false);

        for (Map.Entry<String, String> header : getHeaders(apiKey).entrySet())
        {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        connection.setRequestMethod(method);

        if (method == "POST")
        {
            connection.setDoOutput(true); // Note: this implicitly sets method
                                          // to POST

            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(params);

            OutputStream output = null;
            try
            {
                output = connection.getOutputStream();
                output.write(jsonParams.getBytes("UTF-8"));
            } finally
            {
                if (output != null)
                {
                    output.close();
                }
            }
        }

        return connection;
    }

    private Map<String, String> getHeaders(String apiKey)
    {
        Map<String, String> headers = new HashMap<String, String>();
        String clientStub = String.format("%s-%s", SendWithUs.CLIENT_LANG,
                SendWithUs.CLIENT_VERSION);

        headers.put("Accept", "text/plain");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put(SendWithUs.SWU_API_HEADER, apiKey);
        headers.put(SendWithUs.SWU_CLIENT_HEADER, clientStub);

        return headers;
    }

    private String getResponseBody(HttpURLConnection connection) throws IOException
    {
        int responseCode = connection.getResponseCode();
        InputStream responseStream = null;

        if (responseCode == 200)
        {
            responseStream = connection.getInputStream();
        } else
        {
            responseStream = connection.getErrorStream();
        }

        Scanner responseScanner = new Scanner(responseStream, "UTF-8");

        String responseBody = responseScanner.useDelimiter("\\A").next();

        responseScanner.close();
        responseStream.close();

        return responseBody;
    }

    protected String makeURLRequest(String url, String method) throws SendWithUsException
    {
        return makeURLRequest(url, method, null);
    }

    protected String makeURLRequest(String url, String method, Map<String, Object> params) throws SendWithUsException
    {
        HttpURLConnection connection = null;
        try
        {
            connection = createConnection(url, method, params);
        } catch (IOException e)
        {
            throw new SendWithUsException("Connection error");
        }

        try
        {
            int responseCode = connection.getResponseCode();

            if (responseCode < 200 || responseCode >= 300)
            {
                switch (responseCode) {
                case 400:
                    throw new SendWithUsException("Bad request: "
                            + getResponseBody(connection));
                case 403:
                    throw new SendWithUsException("Authentication error");
                case 404:
                    throw new SendWithUsException("Resource not found");
                default:
                    throw new SendWithUsException(String.format(
                            "Unknown error %d, contact api@sendwithus.com",
                            responseCode));
                }
            }
        } catch (IOException e)
        {
            throw new SendWithUsException("Caught IOException");
        }

        String response = "";
        try
        {
            response = getResponseBody(connection);
        } catch (IOException e)
        {
            throw new SendWithUsException("Caught IOException in response");
        }

        return response;
    }

    /**
     * PUBLIC METHODS
     */

    /**
     * Fetches all available Email templates.
     * 
     * @return Array of Email IDs and names
     * @throws SendWithUsException
     * 
     * @deprecated use templates() instead.
     */
    @Deprecated
    public Email[] emails() throws SendWithUsException
    {
        String url = getURLEndpoint("emails");

        String response = makeURLRequest(url, "GET", null);

        Gson gson = new Gson();
        return gson.fromJson(response, Email[].class);
    }

    /**
     * Fetches all available Email templates.
     * 
     * @return Array of Email IDs and names
     * @throws SendWithUsException
     */
    public Email[] templates() throws SendWithUsException
    {
        String url = getURLEndpoint("templates");

        String response = makeURLRequest(url, "GET", null);

        Gson gson = new Gson();
        return gson.fromJson(response, Email[].class);
    }

    /**
     * Sends an Email. Represents the minimum required arguments for sending an
     * Email.
     * 
     * @param emailId
     *            The Email template's ID
     * @param recipient
     *            Map defining the Recipient
     * @param emailData
     *            Map defining the Email's variable substitutions
     * @return The receipt ID
     * @throws SendWithUsException
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> emailData) throws SendWithUsException
    {
        return this.send(emailId, recipient, null, emailData);
    }

    /**
     * Sends an Email. Includes Sender as a parameter.
     * 
     * @param emailId
     *            The Email template's ID
     * @param recipient
     *            Map defining the Recipient
     * @param sender
     *            Map defining the Sender
     * @param emailData
     *            Map defining the Email's variable substitutions
     * @return The receipt ID
     * @throws SendWithUsException
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData)
            throws SendWithUsException
    {
        return this.send(emailId, recipient, sender, emailData, null);
    }

    /**
     * Sends an Email. Includes CC Recipients as a parameter.
     * 
     * @param emailId
     *            The Email template's ID
     * @param recipient
     *            Map defining the Recipient
     * @param sender
     *            Map defining the Sender
     * @param emailData
     *            Map defining the Email's variable substitutions
     * @param cc
     *            Array of maps defining CC recipients
     * @return The receipt ID
     * @throws SendWithUsException
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData,
            Map<String, Object>[] cc) throws SendWithUsException
    {
        return this.send(emailId, recipient, sender, emailData, cc, null);
    }

    /**
     * Sends an Email. Includes BCC Recipients as a parameter.
     * 
     * @param emailId
     *            The Email template's ID
     * @param recipient
     *            Map defining the Recipient
     * @param sender
     *            Map defining the Sender
     * @param emailData
     *            Map defining the Email's variable substitutions
     * @param cc
     *            Array of maps defining CC recipients
     * @param bcc
     *            Array of maps defining BCC recipients
     * @return The receipt ID
     * @throws SendWithUsException
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData,
            Map<String, Object>[] cc, Map<String, Object>[] bcc)
            throws SendWithUsException
    {
        return this.send(emailId, recipient, sender, emailData, cc, bcc, null);
    }

    /**
     * Sends an Email. Includes attachment filepaths as a parameter.
     * 
     * @param emailId
     *            The Email template's ID
     * @param recipient
     *            Map defining the Recipient
     * @param sender
     *            Map defining the Sender
     * @param emailData
     *            Map defining the Email's variable substitutions
     * @param cc
     *            Array of maps defining CC recipients
     * @param bcc
     *            Array of maps defining BCC recipients
     * @param attachment_paths
     *            Array of filepaths for attachments
     * @return The receipt ID
     * @throws SendWithUsException
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData,
            Map<String, Object>[] cc, Map<String, Object>[] bcc,
            String[] attachment_paths) throws SendWithUsException
    {
        return this.send(emailId, recipient, sender, emailData, cc, bcc,
                attachment_paths, null);
    }

    /**
     * Sends an Email. Includes ESP account as a parameter.
     * 
     * @param emailId
     *            The Email template's ID
     * @param recipient
     *            Map defining the Recipient
     * @param sender
     *            Map defining the Sender
     * @param emailData
     *            Map defining the Email's variable substitutions
     * @param cc
     *            Array of maps defining CC recipients
     * @param bcc
     *            Array of maps defining BCC recipients
     * @param attachment_paths
     *            Array of filepaths for attachments
     * @param espAccount
     *            ID specifying the ESP account to use
     * @return The receipt ID
     * @throws SendWithUsException
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData,
            Map<String, Object>[] cc, Map<String, Object>[] bcc,
            String[] attachment_paths, String espAccount)
            throws SendWithUsException
    {
        SendWithUsSendRequest request = new SendWithUsSendRequest();
        request.setEmailId(emailId).setRecipient(recipient)
                .setEmailData(emailData).setSender(sender).setCcRecipients(cc)
                .setBccRecipients(bcc).setAttachmentPaths(attachment_paths)
                .setEspAccount(espAccount);

        return this.send(request);
    }

    /**
     * Sends an Email defined by a request object.
     * 
     * @param request
     *            The "send" request parameters
     * @return The receipt ID
     * @throws SendWithUsException
     */
    public SendReceipt send(SendWithUsSendRequest request)
            throws SendWithUsException
    {
        Map<String, Object> sendParams = request.asMap();

        String url = getURLEndpoint("send");

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, SendReceipt.class);
    }

    /**
     * Renders a template with the given data.
     * 
     * @param templateId
     *            The Email template ID
     * @param templateData
     *            The template data
     * @return The rendered template
     * @throws SendWithUsException
     */
    public RenderedTemplate render(String templateId,
            Map<String, Object> templateData) throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("template_id", templateId);
        sendParams.put("template_data", templateData);

        String url = getURLEndpoint("render");

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, RenderedTemplate.class);
    }

    /**
     * Activates a drip campaign for a specific email.
     *
     * @param dripCampaignId
     *            The id of the drip campaign
     * @param dripRequest
     *            The "drip activate" request parameters
     * @return Response details
     * @throws SendWithUsException
     */
    public ActivatedDrip startOnDripCampaign(String dripCampaignId, SendWithUsDripRequest dripRequest)
            throws SendWithUsException
    {
        Map<String, Object> sendParams = dripRequest.asMap();

        String url = getURLEndpoint("drip_campaigns");
        url = String.format("%s/%s/activate", url, dripCampaignId);

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, ActivatedDrip.class);
    }

    /**
     * Deactivate a drip campaign for a customer.
     *
     * @param customerEmailAddress
     *            The customer's Email address
     * @param dripCampaignId
     *            The id of the drip campaign
     * @return Response details
     * @throws SendWithUsException
     */
    public DeactivatedDrip removeFromDripCampaign(String customerEmailAddress, String dripCampaignId )
            throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("recipient_address", customerEmailAddress);

        String url = getURLEndpoint("drip_campaigns");
        url = String.format("%s/%s/deactivate", url, dripCampaignId);

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, DeactivatedDrip.class);
    }


    /**
     * Deactivate drip campaigns for a customer.
     * 
     * @param customerEmailAddress
     *            The customer's Email address
     * @return Response details
     * @throws SendWithUsException
     */
    public DeactivatedDrips deactivateDrips(String customerEmailAddress)
            throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("email_address", customerEmailAddress);

        String url = getURLEndpoint("drip_campaigns/deactivate");

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, DeactivatedDrips.class);
    }

    /**
     * Creates or updates a customer record based on Email address.
     * 
     * @param customerEmailAddress
     *            The customer's Email address
     * @param customerData
     *            The customer's data
     * @return Response details
     * @throws SendWithUsException
     */
    public CustomerReceipt createUpdateCustomer(String customerEmailAddress,
            Map<String, Object> customerData)
            throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("email", customerEmailAddress);
        sendParams.put("data", customerData);

        String url = getURLEndpoint("customers");

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, CustomerReceipt.class);
    }


    /**
     * This will add a new conversion with revenue to a specific customer in your sendwithus account.
     *
     * @param customerEmailAddress
     *            The customer's Email address
     * @param revenue
     *            Amount of revenue associated with customer event (Note: in cents, $10.00 = 1000)
     * @return Response details
     * @throws SendWithUsException
     */
    public APIReceipt createConversionEvent(String customerEmailAddress, Integer revenue)
        throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("revenue", revenue);

        String url = getURLEndpoint("customers");
        url = String.format("%s/%s/conversions", url, customerEmailAddress);

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, APIReceipt.class);
    }

    /**
     * This will add a new conversion without revenue to a specific customer in your sendwithus account.
     *
     * @param customerEmailAddress
     *            The customer's Email address
     * @return Response details
     * @throws SendWithUsException
     */
    public APIReceipt createConversionEvent(String customerEmailAddress)
            throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();

        String url = getURLEndpoint("customers");
        url = String.format("%s/%s/conversions", url, customerEmailAddress);

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, APIReceipt.class);
    }


    /**
     * Gets all snippets for your account.
     * 
     * @return Array of Snippet objects
     * @throws SendWithUsException
     */
    public Snippet[] getSnippets() throws SendWithUsException {
        String url = getURLEndpoint("snippets");

        String response = makeURLRequest(url, "GET");

        Gson gson = new Gson();
        return gson.fromJson(response, Snippet[].class);
    }
    
    /**
     * Get a specific snippet for your account.
     * 
     * @param snippetId
     *              The snippet's ID
     * @return Snippet object
     * @throws SendWithUsException
     */
    public Snippet getSnippet(String snippetId) throws SendWithUsException {
        String resource = String.format("snippets/%s", snippetId);
        String url = getURLEndpoint(resource);

        String response = makeURLRequest(url, "GET");

        Gson gson = new Gson();
        return gson.fromJson(response, Snippet.class);
    }
    
    /**
     * Create a new snippet.  Duplicate snippet names are not allowed.
     * 
     * @param name
     *              The snippet's name
     * @param body
     *              The snippet's contents
     * @return SnippetReceipt object
     * @throws SendWithUsException
     */
    public SnippetReceipt createSnippet(String name, String body) throws SendWithUsException {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("name", name);
        sendParams.put("body", body);

        String url = getURLEndpoint("snippets");

        String response = makeURLRequest(url, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, SnippetReceipt.class);
    }
    
    /**
     * Update an existing snippet.  Duplicate snippet names are not allowed.
     * 
     * @param snippetId
     *              The snippet's ID
     * @param name
     *              The snippet's new name
     * @param body
     *              The snippet's new contents
     * @return SnippetReceipt object
     * @throws SendWithUsException
     */
    public SnippetReceipt updateSnippet(String snippetId, String name, String body) throws SendWithUsException {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("name", name);
        sendParams.put("body", body);
        
        String resource = String.format("snippets/%s", snippetId);
        String url = getURLEndpoint(resource);

        String response = makeURLRequest(url, "PUT", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, SnippetReceipt.class);
    }
    
    /**
     * Delete an existing snippet.
     * 
     * @param snippetId
     *              The snippet's ID
     * @return APIReceipt object
     * @throws SendWithUsException
     */
    public APIReceipt deleteSnippet(String snippetId) throws SendWithUsException {
        String resource = String.format("snippets/%s", snippetId);
        String url = getURLEndpoint(resource);

        String response = makeURLRequest(url, "DELETE");

        Gson gson = new Gson();
        return gson.fromJson(response, APIReceipt.class);
    }

}
