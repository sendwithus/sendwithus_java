package com.sendwithus;

import java.util.HashMap;
import java.util.Map;

/**
 * POJO encapsulating parameters for SendWithUs "drip activate" API calls.
 */
public class SendWithUsDripRequest
{
    private Map<String, Object> recipient = null;
    private Map<String, Object> emailData = null;
    private Map<String, Object>[] ccRecipients = null;
    private Map<String, Object>[] bccRecipients = null;
    private Map<String, Object> sender = null;
    private String[] tags = null;
    private String espAccount = null;
    private String locale = null;

    public SendWithUsDripRequest()
    {
    }

    /**
     * @return HashMap of request parameters for "drip activate" API call
     */
    public Map<String, Object> asMap()
    {
        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("recipient", recipient);
        sendParams.put("email_data", emailData);

        // sender is optional
        if (sender != null)
        {
            sendParams.put("sender", sender);
        }

        // cc is optional
        if ((ccRecipients != null) && (ccRecipients.length > 0))
        {
            sendParams.put("cc", ccRecipients);
        }

        // bcc is optional
        if ((bccRecipients != null) && (bccRecipients.length > 0))
        {
            sendParams.put("bcc", bccRecipients);
        }

        if ((tags != null) && (tags.length > 0))
        {
            sendParams.put("tags", tags);
        }

        if (espAccount != null)
        {
            sendParams.put("esp_account", espAccount);
        }

        if (locale != null)
        {
            sendParams.put("locale", locale);
        }

        return sendParams;
    }

    public SendWithUsDripRequest setRecipient(Map<String, Object> recipient)
    {
        this.recipient = recipient;
        return this;
    }

    public SendWithUsDripRequest setEmailData(Map<String, Object> emailData)
    {
        this.emailData = emailData;
        return this;
    }

    public SendWithUsDripRequest setCcRecipients(
            Map<String, Object>[] ccRecipients)
    {
        this.ccRecipients = ccRecipients;
        return this;
    }

    public SendWithUsDripRequest setBccRecipients(
            Map<String, Object>[] bccRecipients)
    {
        this.bccRecipients = bccRecipients;
        return this;
    }

    public SendWithUsDripRequest setSender(Map<String, Object> sender)
    {
        this.sender = sender;
        return this;
    }

    public SendWithUsDripRequest setTags(String[] tags)
    {
        this.tags = tags;
        return this;
    }

    public SendWithUsDripRequest setEspAccount(String espAccount)
    {
        this.espAccount = espAccount;
        return this;
    }

    public SendWithUsDripRequest setLocale(String locale)
    {
        this.locale = locale;
        return this;
    }
}
