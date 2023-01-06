package org.example.framework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mashape.unirest.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.example.utils.logger.ILogger;

import java.io.*;
import java.util.Date;
import java.util.stream.Collectors;

public class HTTPResponseHandler implements ILogger {

    private Long timeTaken;

    private HttpResponse<?> uniRestHttpResponse;

    private HTTPResponse response = new HTTPResponse();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Date.class, UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .registerTypeAdapter(java.util.Date.class, UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();

    protected HTTPResponseHandler(HttpResponse<?> uniRestHttpResponse) {
        this.uniRestHttpResponse = uniRestHttpResponse;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public HTTPResponse getResponse() {
        if (uniRestHttpResponse != null) {
            response = new HTTPResponse();
            HTTPBody httpBody = new HTTPBody();
            httpBody.setRawBody(uniRestHttpResponse.getRawBody());
            httpBody.setBodyText(uniRestHttpResponse.getBody().toString());
            response.setBody(httpBody);
            response.setStatusCode(uniRestHttpResponse.getStatus());
            response.setReasonPhrase(uniRestHttpResponse.getStatusText());
            response.setResponseHeaders(uniRestHttpResponse.getHeaders()
                    .keySet().stream().collect(Collectors.toMap(a -> a, a -> uniRestHttpResponse.getHeaders().get(a))));

            String contentType = getContentType();

            if (contentType != null)
                response.setContentType(contentType);
            if (uniRestHttpResponse.getBody() != null && uniRestHttpResponse.getBody() instanceof String) {
                if (contentType != null && (contentType.contains("xml") || contentType.contains("html")))
                    response.getBody().setBodyText(StringEscapeUtils.unescapeHtml4((String) uniRestHttpResponse.getBody()));
            } else {
                response.getBody().setBodyText((String) uniRestHttpResponse.getBody());
            }
            if (timeTaken != null) {
                response.setTimeTaken(timeTaken);
            }
        }
        return response;

    }

    private String getContentType(){
        return (uniRestHttpResponse == null || uniRestHttpResponse.getHeaders()==null) ? null
                : (uniRestHttpResponse.getHeaders().getFirst("Content-Type") !=null
                ? uniRestHttpResponse.getHeaders().getFirst("Content-Type")
                : (uniRestHttpResponse.getHeaders().getFirst("content-type") !=null
                ? uniRestHttpResponse.getHeaders().getFirst("content-type")
                : null));
    }

    public HTTPStatus getStatusLine(){
        return new HTTPStatus(uniRestHttpResponse != null ? uniRestHttpResponse.getStatus() : -1,
                uniRestHttpResponse != null ? uniRestHttpResponse.getStatusText() : null) {
        };
    }

    public String getResponseAsString(){
        return (uniRestHttpResponse!=null && uniRestHttpResponse.getBody()!=null
                ? (String) uniRestHttpResponse.getBody() : null);
    }

}


final class UnixEpochDateTypeAdapter extends TypeAdapter<Date> {

    private static final TypeAdapter<Date> unixEpochDateTypeAdapter = new UnixEpochDateTypeAdapter();

    private UnixEpochDateTypeAdapter() {
    }

    static TypeAdapter<Date> getUnixEpochDateTypeAdapter() {
        return unixEpochDateTypeAdapter;
    }

    @Override
    public Date read(final JsonReader in) throws IOException {
        return new Date(in.nextLong());
    }

    @Override
    public void write(final JsonWriter out, final Date value) throws IOException {
        out.value(value.getTime());
    }
}