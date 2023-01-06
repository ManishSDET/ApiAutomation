package org.example.framework;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import javax.swing.text.Document;
import java.io.InputStream;

public class HTTPBody {

    private String bodyText;

    private DocumentContext path;

    private Document xmlDoc;

    private InputStream rawBody;

    public String getBodyText(){ return bodyText;}

    public void setBodyText(String bodyText){
        this.bodyText = bodyText;
    }

    public <T> T path(String jsonQueryPath){
        if(path==null)
            path = JsonPath.parse(jsonQueryPath);
        return path.read(jsonQueryPath);
    }

    public InputStream getRawBody() { return rawBody;}

    public void setRawBody(InputStream rawBody){ this.rawBody = rawBody;}

    @Override
    public String toString(){ return "";}

}
