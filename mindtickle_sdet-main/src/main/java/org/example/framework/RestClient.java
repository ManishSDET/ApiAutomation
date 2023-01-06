package org.example.framework;


import org.example.utils.logger.ILogger;

public interface RestClient extends ILogger {

    public String getBaseUrl();

    public void createRequest(String serviceName, HTTPRequestSpecification httpRequestSpecification, String endPoint,
                                    String protocol);

    public HTTPResponseHandler get();

    public HTTPResponseHandler post();

    public HTTPResponseHandler put();

    public HTTPResponseHandler delete();

    public HTTPResponseHandler head();

    public HTTPResponseHandler options();

    public HTTPResponseHandler patch();

    public HTTPResponseHandler uploadFiles();

    public HTTPResponseHandler downloadFiles();
}