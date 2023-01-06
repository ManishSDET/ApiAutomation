package org.example.framework;

import java.util.HashMap;
import java.util.Map;

public class HTTPHeader {

    private Map<String, String> headers = new HashMap<>();

    public Map<String, String> getHeaders(){ return this.headers;}

    public void setHeaders(Map<String, String> headers){ this.headers = headers;}

    public void addHeader(String key, String value){ this.headers.put(key, value);}

    public String getHeader(String key){ return this.headers.get(key);}

    @Override
    public String toString(){ return "HTTP Header=[" + headers + "]";}
}
