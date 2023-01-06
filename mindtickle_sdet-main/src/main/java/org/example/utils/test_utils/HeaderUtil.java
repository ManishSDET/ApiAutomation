package org.example.utils.test_utils;

import org.example.framework.HTTPHeader;

import java.util.HashMap;
import java.util.Map;

public class HeaderUtil {

    public HTTPHeader createHeaderForJSON(){
        Map<String, String> header = new HashMap<>();
        header.put("content-type", "application/JSON");
        header.put("accept-type", "application/Json");
        HTTPHeader httpHeader = new HTTPHeader();
        httpHeader.setHeaders(header);
        return httpHeader;
    }
}
