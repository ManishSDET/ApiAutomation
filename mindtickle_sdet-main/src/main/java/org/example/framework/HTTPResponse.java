package org.example.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPResponse {

    private String contentType;

    private int statusCode;

    private String reasonPhrase;

    private Map<String, List<String>> responseHeaders = new HashMap<>();

    private long timeTaken;

    private HTTPBody body;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public HTTPBody getBody() {
        return body;
    }

    public void setBody(HTTPBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HTTPResponse [contentType=" + contentType + ", statusCode=" + statusCode + ", reasonPhrase="
                + reasonPhrase + ", responseHeaders=" + responseHeaders + ", timeTaken=" + timeTaken + ", body=" + body
                + "]";
    }

}

