package com.sendwithus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.sendwithus.exception.SendWithUsException;


public class SendWithUs
{
    public static final String API_PROTO = "https";
    public static final String API_HOST = "beta.sendwithus.com";
    public static final String API_PORT = "443";
    public static final String API_VERSION = "0";

    private String apiKey;

    public SendWithUs(String apiKey) {
        this.apiKey = apiKey;
    }

    public void send(String emailID, String emailTo, Map<String, Object> emailData) 
            throws SendWithUsException
    {

        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("email_id", emailID);
        sendParams.put("email_to", emailTo);
        sendParams.put("email_data", emailData);

        String url = getURLEndpoint("send");

        makeURLRequest(url, this.apiKey, sendParams);
    }

    private static String getURLEndpoint(String resourceName) {
        return String.format("%s://%s:%s/api/v%s/%s", 
            SendWithUs.API_PROTO, SendWithUs.API_HOST, 
            SendWithUs.API_PORT, SendWithUs.API_VERSION, 
            resourceName);
    }

    private static javax.net.ssl.HttpsURLConnection createConnection(
            String url, String apiKey, Map<String, Object> params) 
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
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

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

        return connection;
    }

    private static Map<String, String> getHeaders(String apiKey) {
        Map<String, String> headers = new HashMap<String, String>();
        
        headers.put("Accept", "text/plain");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("X-SWU-API-KEY", apiKey);

        return headers;
    }

    private static String jsonifyParams(Map<?, ?> params) {
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

    private static void makeURLRequest(
            String url, String apiKey, Map<String, Object> params)
            throws SendWithUsException
    {
        javax.net.ssl.HttpsURLConnection connection = null;
        try {
            connection = createConnection(url, apiKey, params);
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
                    throw new SendWithUsException("Unknown error, contact api@sendwithus.com");
                }
            }
        } catch (IOException e) {
            throw new SendWithUsException("Caught IOException");
        }
    }
}
