package com.sendwithus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.sendwithus.exception.SendWithUsException;

/**
 * POJO encapsulating parameters for SendWithUs "send" API calls.
 */
public class SendWithUsSendRequest
{

    private String emailId = null;
    private Map<String, Object> recipient = null;
    private Map<String, Object> emailData = null;
    private Map<String, Object>[] ccRecipients = null;
    private Map<String, Object>[] bccRecipients = null;
    private Map<String, Object> sender = null;
    private String[] tags = null;
    private Map<String, String> inline = null;
    private String[] attachmentPaths = null;
    private String espAccount = null;
    private String versionName = null;

    public SendWithUsSendRequest()
    {
    }

    /**
     * @return HashMap of request parameters for "send" API call
     * @throws SendWithUsException
     */
    public Map<String, Object> asMap() throws SendWithUsException
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
        if ((ccRecipients != null) && (ccRecipients.length > 0))
        {
            sendParams.put("cc", ccRecipients);
        }

        // bcc is optional
        if ((bccRecipients != null) && (bccRecipients.length > 0))
        {
            sendParams.put("bcc", bccRecipients);
        }

        if ((attachmentPaths != null) && (attachmentPaths.length > 0))
        {
            ArrayList<HashMap<String, String>> files = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < attachmentPaths.length; i++)
            {
                try
                {
                    File file = new File(attachmentPaths[i]);
                    byte[] byteArray = FileUtils.readFileToByteArray(file);
                    byte[] encodedBytes = Base64.encodeBase64(byteArray);

                    HashMap<String, String> file_map = new HashMap<String, String>();

                    file_map.put("id",
                            FilenameUtils.getName(attachmentPaths[i]));
                    file_map.put("data", new String(encodedBytes));

                    files.add(file_map);

                } catch (IOException e)
                {
                    throw new SendWithUsException("Caught IOException");
                }
            }
            sendParams.put("files", files);
        }

        if ((tags != null) && (tags.length > 0))
        {
            sendParams.put("tags", tags);
        }

        if (inline != null)
        {
            sendParams.put("inline", inline);
        }

        if (espAccount != null)
        {
            sendParams.put("esp_account", espAccount);
        }

        if (versionName != null)
        {
            sendParams.put("version_name", versionName);
        }

        return sendParams;
    }

    public SendWithUsSendRequest setEmailId(String emailId)
    {
        this.emailId = emailId;
        return this;
    }

    public SendWithUsSendRequest setRecipient(Map<String, Object> recipient)
    {
        this.recipient = recipient;
        return this;
    }

    public SendWithUsSendRequest setEmailData(Map<String, Object> emailData)
    {
        this.emailData = emailData;
        return this;
    }

    public SendWithUsSendRequest setCcRecipients(
            Map<String, Object>[] ccRecipients)
    {
        this.ccRecipients = ccRecipients;
        return this;
    }

    public SendWithUsSendRequest setBccRecipients(
            Map<String, Object>[] bccRecipients)
    {
        this.bccRecipients = bccRecipients;
        return this;
    }

    public SendWithUsSendRequest setSender(Map<String, Object> sender)
    {
        this.sender = sender;
        return this;
    }

    public SendWithUsSendRequest setTags(String[] tags)
    {
        this.tags = tags;
        return this;
    }

    public SendWithUsSendRequest setInline(Map<String, String> inline)
    {
        this.inline = inline;
        return this;
    }

    public SendWithUsSendRequest setAttachmentPaths(String[] attachmentPaths)
    {
        this.attachmentPaths = attachmentPaths;
        return this;
    }

    public SendWithUsSendRequest setEspAccount(String espAccount)
    {
        this.espAccount = espAccount;
        return this;
    }

    public SendWithUsSendRequest setVersionName(String versionName)
    {
        this.versionName = versionName;
        return this;
    }

}
