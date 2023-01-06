package org.example.utils.test_utils;

import org.example.framework.*;

import static org.example.utils.logger.ILogger.LOG;

public class APIUtil {

    public HTTPResponseHandler createRequest(String serviceName , HTTPMethod httpMethod, HTTPRequestSpecification httpRequestSpecification,
                                             String endpoint, String protocol){
        UniRestClient uniRestClient = new UniRestClient();
        HTTPResponseHandler httpResponseHandler = null;
        try {
            uniRestClient.createRequest(serviceName, httpRequestSpecification, endpoint, protocol);
            httpResponseHandler = uniRestClient.sendRequest(httpMethod);
        }catch (Exception ex){
            LOG.info("Exception :" + ex);
        }
        return httpResponseHandler;
    }
}
