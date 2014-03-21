package com.sendwithus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.DeactivatedDrips;
import com.sendwithus.model.Email;
import com.sendwithus.model.SendReceipt;
import com.sendwithus.model.RenderedTemplate;


public class SendWithUs
{
    public static final String API_PROTO = "https";
    public static final String API_HOST = "api.sendwithus.com";
    public static final String API_PORT = "443";
    public static final String API_VERSION = "1";
    public static final String CLIENT_VERSION = "1.0.7";
    public static final String CLIENT_LANG = "java";
    public static final String SWU_API_HEADER = "X-SWU-API-KEY";
    public static final String SWU_CLIENT_HEADER = "X-SWU-API-CLIENT";

    private String apiKey;

    public SendWithUs(String apiKey)
    {
        this.apiKey = apiKey;
    }

    private static String getURLEndpoint(String resourceName)
    {
        return String.format("%s://%s:%s/api/v%s/%s",
            SendWithUs.API_PROTO, SendWithUs.API_HOST,
            SendWithUs.API_PORT, SendWithUs.API_VERSION,
            resourceName);
    }

    private static javax.net.ssl.HttpsURLConnection createConnection(
            String url, String apiKey, String method, Map<String, Object> params)
            throws IOException
    {

        URL connectionURL = new URL(url);
        javax.net.ssl.HttpsURLConnection connection =
            (javax.net.ssl.HttpsURLConnection) connectionURL.openConnection();
        connection.setConnectTimeout(30000); // 30 seconds
        connection.setReadTimeout(60000); // 60 seconds
        connection.setUseCaches(false);

        for (Map.Entry<String, String> header : getHeaders(apiKey).entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        connection.setRequestMethod(method);

        if (method == "POST") {
            connection.setDoOutput(true); // Note: this implicitly sets method to POST

            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(params);

            OutputStream output = null;
            try {
                output = connection.getOutputStream();
                output.write(jsonParams.getBytes("UTF-8"));
            } finally {
                if (output != null) {
                    output.close();
                }
            }
        }

        return connection;
    }

    private static Map<String, String> getHeaders(String apiKey)
    {
        Map<String, String> headers = new HashMap<String, String>();
        String clientStub = String.format("%s-%s", SendWithUs.CLIENT_LANG, SendWithUs.CLIENT_VERSION);

        headers.put("Accept", "text/plain");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put(SendWithUs.SWU_API_HEADER, apiKey);
        headers.put(SendWithUs.SWU_CLIENT_HEADER, clientStub);

        return headers;
    }

    private static String getResponseBody(javax.net.ssl.HttpsURLConnection connection)
            throws IOException
    {

        int responseCode = connection.getResponseCode();
        InputStream responseStream = null;

        if (responseCode == 200) {
            responseStream = connection.getInputStream();
        } else {
            responseStream = connection.getErrorStream();
        }

        String responseBody = new Scanner(responseStream, "UTF-8")
            .useDelimiter("\\A")
            .next();
        responseStream.close();

        return responseBody;
    }

    private static String makeURLRequest(
            String url, String apiKey, String method, Map<String, Object> params)
            throws SendWithUsException
    {
        javax.net.ssl.HttpsURLConnection connection = null;
        try {
            connection = createConnection(url, apiKey, method, params);
        } catch (IOException e) {
            throw new SendWithUsException("Connection error");
        }

        try {
            int responseCode = connection.getResponseCode();

            if (responseCode < 200 || responseCode >= 300) {
                switch (responseCode) {
                case 400:
                    throw new SendWithUsException("Bad request: " + getResponseBody(connection));
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
        } catch (IOException e) {
            throw new SendWithUsException("Caught IOException");
        }

        String response = "";
        try {
            response = getResponseBody(connection);
        } catch (IOException e) {
            throw new SendWithUsException("Caught IOException in response");
        }

        return response;
    }

    /**
     * PUBLIC METHODS
     */

    public Email[] emails()
        throws SendWithUsException
    {
        String url = getURLEndpoint("emails");

        String response = makeURLRequest(url, this.apiKey, "GET", null);

        Gson gson = new Gson();
        return gson.fromJson(response, Email[].class);
    }

    /**
     * send
     * String emailId
     * Map<String, Object> recipient
     * Map<String, Object> emailData
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> emailData)
            throws SendWithUsException
    {
        return this.send(emailId, recipient, null, emailData);
    }

    /**
     * send
     * String emailId
     * Map<String, Object> recipient
     * Map<String, Object> sender
     * Map<String, Object> emailData
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData)
            throws SendWithUsException
    {
        return this.send(emailId, recipient, sender, emailData, null, null);
    }

    /**
     * send
     * String emailId
     * Map<String, Object> recipient
     * Map<String, Object> sender
     * Map<String, Object> emailData
     * Array(Map<String, Object>) cc
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData,
            Map<String, Object>[] cc)
            throws SendWithUsException
    {
        return this.send(emailId, recipient, sender, emailData, cc, null);
    }

    /**
     * send
     * String emailId
     * Map<String, Object> recipient
     * Map<String, Object> sender
     * Map<String, Object> emailData
     * Array(Map<String, Object>) cc
     * Array(Map<String, Object>) bcc
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData,
            Map<String, Object>[] cc, Map<String, Object>[] bcc)
            throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("email_id", emailId);
        sendParams.put("recipient", recipient);
        sendParams.put("email_data", emailData);

        // sender is optional
        if (sender != null)
        {
            sendParams.put("sender", sender);
        }

        // cc is optional
        if (cc != null)
        {
            sendParams.put("cc", cc);
        }

        // bcc is optional
        if (bcc != null)
        {
            sendParams.put("bcc", bcc);
        }

        String url = getURLEndpoint("send");

        String response = makeURLRequest(url, this.apiKey, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, SendReceipt.class);
    }

    /**
     * send
     * String emailId
     * Map<String, Object> recipient
     * Map<String, Object> sender
     * Map<String, Object> emailData
     * Array(Map<String, Object>) cc
     * Array(Map<String, Object>) bcc
     * String attachment
     */
    public SendReceipt send(String emailId, Map<String, Object> recipient,
            Map<String, Object> sender, Map<String, Object> emailData,
            Map<String, Object>[] cc, Map<String, Object>[] bcc, String[] attachment_paths)
            throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("email_id", emailId);
        sendParams.put("recipient", recipient);
        sendParams.put("email_data", emailData);

        // sender is optional
        if (sender != null)
        {
            sendParams.put("sender", sender);
        }

        // cc is optional
        if (cc != null)
        {
            sendParams.put("cc", cc);
        }

        // bcc is optional
        if (bcc != null)
        {
            sendParams.put("bcc", bcc);
        }

        if (attachment_paths != null)
        {
            ArrayList<HashMap> files = new ArrayList<HashMap>();

            for (int i = 0; i < attachment_paths.length; i++) {
                try {
                    File file = new File(attachment_paths[i]);
                    byte[] byteArray = FileUtils.readFileToByteArray(file);
                    byte[] encodedBytes = Base64.encodeBase64(byteArray);

                    HashMap<String, String> file_map = new HashMap<String, String>();

                    file_map.put("id", FilenameUtils.getName(attachment_paths[i]));
                    file_map.put("data", new String(encodedBytes));

                    files.add(file_map);

                } catch (IOException e) {
                    throw new SendWithUsException("Caught IOException");
                }
            }
            sendParams.put("files", files);
        }

        String url = getURLEndpoint("send");

        String response = makeURLRequest(url, this.apiKey, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, SendReceipt.class);
    }

    /**
     * render
     * String emailId
     * Map<String, Object> emailData
     */
    public RenderedTemplate render(String templateId, Map<String, Object> templateData)
            throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("template_id", templateId);
        sendParams.put("template_data", templateData);

        String url = getURLEndpoint("render");

        String response = makeURLRequest(url, this.apiKey, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, RenderedTemplate.class);
    }

    /**
     * deactivateDrips
     * String customerEmailAddress
     */
    public DeactivatedDrips deactivateDrips(String customerEmailAddress)
        throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("email_address", customerEmailAddress);

        String url = getURLEndpoint("drips/deactivate");

        String response = makeURLRequest(url, this.apiKey, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, DeactivatedDrips.class);
    }
}
