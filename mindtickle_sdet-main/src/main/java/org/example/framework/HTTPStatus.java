package org.example.framework;

public class HTTPStatus {

    private int statusCode;

    private String reasonPhrase;

    public HTTPStatus(int statusCode, String reasonPhrase){
        this.statusCode=statusCode;
        this.reasonPhrase=reasonPhrase;
    }

    public int getStatusCode(){ return statusCode;}

    public void setStatusCode(int statusCode){ this.statusCode=statusCode;}

    public String getReasonPhrase(){ return reasonPhrase;}

    public void setReasonPhrase(String reasonPhrase){ this.reasonPhrase=reasonPhrase;}

    @Override
    public String toString(){ return "HTTPStatus [StatusCode=" + statusCode+ ", Reason Pharse="+ reasonPhrase+"]";}
}
