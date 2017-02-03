package com.sendwithus;

public class SendWithUsApiLocation
{
    public static final String PROTO = "https";
    public static final String HOST = "api.sendwithus.com";
    public static final String PORT = "443";
    public static final String VERSION = "1";

    private final String apiLocation;

    public SendWithUsApiLocation(String apiLocation)
    {
        this.apiLocation = apiLocation;
    }

    public String getEndpointUrl(String resourceName)
    {
        return String.format("%s/%s", apiLocation, resourceName);
    }

    public static SendWithUsApiLocation defaultApiLocation()
    {
        return new SendWithUsApiLocation(String.format("%s://%s:%s/api/v%s", PROTO, HOST, PORT, VERSION));
    }
}
