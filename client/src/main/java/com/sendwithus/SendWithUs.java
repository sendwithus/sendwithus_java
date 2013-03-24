package com.sendwithus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.Email;
import com.sendwithus.model.SendReceipt;


public class SendWithUs
{
    public static final String API_PROTO = "https";
    public static final String API_HOST = "beta.sendwithus.com";
    public static final String API_PORT = "443";
    public static final String API_VERSION = "1_0";

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

            String jsonParams = jsonifyParams(params);
            
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
        
        headers.put("Accept", "text/plain");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("X-SWU-API-KEY", apiKey);

        return headers;
    }

    private static String jsonifyParams(Map<?, ?> params) 
    {
        if (params == null) {
            return "{}";
        }

        StringBuffer jsonStringBuffer = new StringBuffer();

        jsonStringBuffer.append("{");
        for (Map.Entry<?, ?> entry : params.entrySet()) {
            jsonStringBuffer.append(",\"");
            jsonStringBuffer.append(entry.getKey().toString());
            jsonStringBuffer.append("\":");

            if (entry.getValue() instanceof Map<?, ?>) {
                jsonStringBuffer.append(jsonifyParams((Map<?, ?>) entry.getValue()));
            } else {
                jsonStringBuffer.append(String.format("\"%s\"", entry.getValue().toString()));
            }
        }
        jsonStringBuffer.append("}");
        jsonStringBuffer.deleteCharAt(1);

        return jsonStringBuffer.toString();
    }

    private static String getResponseBody(javax.net.ssl.HttpsURLConnection connection) 
            throws IOException
    {

        InputStream responseStream = connection.getInputStream();
        
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
                    throw new SendWithUsException("Bad request");
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

    public Email[] emails()
        throws SendWithUsException
    {
        String url = getURLEndpoint("emails");

        String response = makeURLRequest(url, this.apiKey, "GET", null);

        Gson gson = new Gson();
        return gson.fromJson(response, Email[].class);
    }

    public SendReceipt send(String emailID, Map<String, Object> recipient, Map<String, Object> emailData) 
            throws SendWithUsException
    {
        return this.send(emailID, recipient, null, emailData);
    }

    public SendReceipt send(String emailID, Map<String, Object> recipient, Map<String, Object> sender, Map<String, Object> emailData) 
            throws SendWithUsException
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("email_id", emailID);
        sendParams.put("recipient", recipient);
        // sender is optional
        if (sender != null)
        {
            sendParams.put("sender", sender);
        }
        sendParams.put("email_data", emailData);

        String url = getURLEndpoint("send");

        String response = makeURLRequest(url, this.apiKey, "POST", sendParams);

        Gson gson = new Gson();
        return gson.fromJson(response, SendReceipt.class);
    }

}
