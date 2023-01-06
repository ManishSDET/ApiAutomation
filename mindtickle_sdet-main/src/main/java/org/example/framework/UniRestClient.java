package org.example.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import io.qameta.allure.Attachment;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Pattern;

public class UniRestClient implements RestClient{

    private static final String PRINT_FORMAT = "%1$-30s%2$-50s\n";
    private static final String NONE = "<none>";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private HTTPRequestSpecification specification;

    private String baseUrl;
    private String uri;
    private String completeUrl;

    static {
        HttpClientBuilder builder = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, false))
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(getSSLCContext());
        Unirest.setHttpClient(builder.build());
    }

    private static SSLContext getSSLCContext(){
        try{
            SSLContextBuilder builder = new SSLContextBuilder().loadTrustMaterial(null,
                    ((x509Certificates, authType) -> true));
            return builder.build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            return null;
        }
    }

    public UniRestClient(){}

    public HTTPResponseHandler sendRequest(final HTTPMethod httpMethod){
        return sendRequest(httpMethod, true);
    }

    private HTTPResponseHandler sendRequest(final HTTPMethod httpMethod, boolean isPrintCompleteResponse) {

        if(httpMethod==null)
            throw new RuntimeException("HTTP Method cannot be null");

        if(uri==null)
            throw new RuntimeException("uri cannot be null");

        HttpRequest request=null;

        switch (httpMethod){
            case GET:
                request = Unirest.get(uri);
                break;
            case POST:
                request = Unirest.post(uri);
                break;
            case PUT:
                request = Unirest.put(uri);
                break;
            case DELETE:
                request = Unirest.delete(uri);
                break;
            case HEAD:
                request=Unirest.head(uri);
                break;
            case OPTIONS:
                request=Unirest.options(uri);
                break;
            case TRACE:
                request=Unirest.patch(uri);
                break;
            default:
                break;
        }

        if (request==null)
            return null;

        appendHeadersAndParams(request, specification);
        this.completeUrl=request.getUrl();
        logRequests(httpMethod, specification);
        HttpResponse<?> response = null;
        long start = System.currentTimeMillis();
        ObjectMapper obj = new ObjectMapper();
        try {
            if (request instanceof GetRequest) {
                response = request.asString();
            } else if (request instanceof HttpRequestWithBody) {
                response = (this.specification.getBody() == null) ? request.asString()
                        : ((HttpRequestWithBody) request).body(getBody(specification)).asString();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        HTTPResponseHandler responseHanders = new HTTPResponseHandler(response);
        long timeTaken = System.currentTimeMillis() - start;
        responseHanders.setTimeTaken(timeTaken);
        logResponse(response, timeTaken);
        return responseHanders;

    }

    @Attachment("response")
    private String logResponse(HttpResponse<?> response, long timeTaken) {
        return logResponse(response, timeTaken, true);
    }

    /**
     * Log Response
     *
     * @param timeTakenInMs
     * @param response
     *            {@link HttpResponse}
     * @param printResponse
     * @return {@link String}
     */

    private String logResponse(HttpResponse<?> response, long timeTakenInMs, boolean printResponse) {
        if (response == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        String prefix = "\n******************************************** response *********************************************"
                + System.lineSeparator();
        builder.append(
                String.format(PRINT_FORMAT, "Status Line: ", response.getStatus() + " " + response.getStatusText()));
        builder.append(String.format(PRINT_FORMAT, "Response Time: ", timeTakenInMs + " ms"));
        builder.append(getHeadersOutput(response.getHeaders()));
        if (printResponse) {

            String contentType = (response == null || response.getHeaders() == null) ? null
                    : (response.getHeaders().getFirst("Content-Type") != null
                    ? response.getHeaders().getFirst("Content-Type")
                    : (response.getHeaders().getFirst("content-type") != null
                    ? response.getHeaders().getFirst("content-type")
                    : null));

            Object body = response.getBody();
            if (body != null) {
                if (body instanceof String) {
                    if (contentType != null && (contentType.contains("xml") || contentType.contains("html"))) {
                        body = StringEscapeUtils.unescapeHtml((String) response.getBody());
                    }
                }
                builder.append(String.format(PRINT_FORMAT, "Body: ", "\n" + prettyPrint(contentType, body)));
            }
        }
        String suffix = "***************************************************************************************************";
        String msg = builder.toString();
        String complete = prefix + msg + suffix;
        System.out.println(complete.replaceAll("\\r?\\n", "<br/>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
        LOG.info(complete);
        return msg;
    }

    /**
     * Pretty Print JSON
     *
     * @param contentType
     * @param body
     *            {@link Object}
     * @return {@link Object}
     */
    private Object prettyPrint(String contentType, Object body) {
        if (contentType == null) {
            return body;
        }
        if (body instanceof String) {
            try {
                return contentType.contains("xml") ? body : GSON.toJson(new JsonParser().parse((String) body));
            } catch (Exception e) {
                return body;
            }
        }
        return body;
    }

    /**
     * Log request
     *
     * @param method
     * @param spec
     * @return
     */
    @Attachment("request")
    private String logRequests(HTTPMethod method, HTTPRequestSpecification spec) {

        if (spec == null) {
            return "";
        }

        String prefix = "\n********************************************* request *********************************************"
                + System.lineSeparator();
        StringBuilder builder = new StringBuilder();

        builder.append(String.format(PRINT_FORMAT, "Request HTTPMethod:", method));
        builder.append(
                String.format(PRINT_FORMAT, "Request URI: ", replacePathVariables(completeUrl, spec.getParams())));
        builder.append(getParamsOutput(spec.getParams()));
        builder.append(getHeadersOutput(spec.getHeaders() == null ? null : spec.getHeaders().getHeaders()));
        builder.append(String.format(PRINT_FORMAT, "Multi-Parts: ",
                spec.getFiles() == null || spec.getFiles().isEmpty() ? NONE : spec.getFiles()));
        builder.append(String.format(PRINT_FORMAT, "Body: ",
                spec.getBody() == null ? NONE
                        : System.lineSeparator() + prettyPrint(spec.getHeaders().getHeader("Content-Type") == null
                        ? spec.getHeaders().getHeader("content-type")
                        : spec.getHeaders().getHeader("Content-Type"), spec.getBody())));
        String suffix = "***************************************************************************************************";

        String complete = prefix + builder + suffix;
        System.out.println(complete.replaceAll("\\r?\\n", "<br/>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
        LOG.info(complete);
        return builder.toString();
    }

    /**
     * Replace all path variables
     *
     * @param url
     *            {@link String}
     * @param params
     *            {@link Params}
     * @return {@link String}
     */
    private String replacePathVariables(String url, Params<?> params) {
        String _url = url;
        if (params instanceof HTTPPathParams) {
            for (Map.Entry<String, String> entry : params.getParams().entrySet()) {
                _url = _url.replaceAll(Pattern.quote("{" + entry.getKey() + "}"), entry.getValue());
            }
        }
        if (params instanceof HTTPHybridParams) {
            for (Map.Entry<String, String> entry : ((HTTPHybridParams) params).getPathParams().getParams().entrySet()) {
                _url = _url.replaceAll(Pattern.quote("{" + entry.getKey() + "}"), entry.getValue());
            }
        }
        return _url;
    }

    /**
     * Get the headers text that need to be printer on the console
     *
     * @param headers
     *            {@link Map}&lt; {@link String}, ?&gt;
     * @return {@link String}
     */
    private String getHeadersOutput(Map<String, ?> headers) {
        if (headers == null || headers.isEmpty()) {
            return NONE;
        }
        StringBuilder builder = new StringBuilder();

        String name = "Headers: ";
        if (headers.isEmpty()) {
            builder.append(String.format(PRINT_FORMAT, name, NONE));
        } else {
            int i = 0;
            for (Map.Entry<String, ?> entry : headers.entrySet()) {
                if (i++ != 0) {
                    builder.append(String.format(PRINT_FORMAT, "", entry.getKey() + " = " + entry.getValue()));
                } else {
                    builder.append(String.format(PRINT_FORMAT, name, entry.getKey() + " = " + entry.getValue()));
                }
            }
        }

        return builder.toString();
    }

    /**
     * Get the params text that need to be printer on the console
     *
     * @param params
     *            {@link Params}&lt;?&gt;
     * @return {@link String}
     */
    private String getParamsOutput(Params<?> params) {

        StringBuilder builder = new StringBuilder();
        if (params instanceof HTTPPathParams || params instanceof HTTPQueryParams || params instanceof HTTPParams) {

            String name = params instanceof HTTPPathParams ? "Path"
                    : (params instanceof HTTPQueryParams ? "Query" : "Form");
            if (params.getParams().isEmpty()) {
                builder.append(String.format(PRINT_FORMAT, name + " Params: ", NONE));
            } else {
                int i = 0;
                for (Map.Entry<String, String> entry : params.getParams().entrySet()) {
                    if (i++ != 0) {
                        builder.append(String.format(PRINT_FORMAT, "", entry.getKey() + " = " + entry.getValue()));
                    } else {
                        builder.append(String.format(PRINT_FORMAT, name + " Params: ",
                                entry.getKey() + " = " + entry.getValue()));
                    }
                }
            }
        } else if (params instanceof HTTPHybridParams) {

            HTTPHybridParams httpHybridParams = (HTTPHybridParams) params;

            String name = "Query";
            if (httpHybridParams.getQueryParams().getParams().isEmpty()) {
                builder.append(String.format(PRINT_FORMAT, name + " Params: ", NONE));
            } else {
                int i = 0;
                for (Map.Entry<String, String> entry : httpHybridParams.getQueryParams().getParams().entrySet()) {
                    if (i++ != 0) {
                        builder.append(String.format(PRINT_FORMAT, "", entry.getKey() + " = " + entry.getValue()));
                    } else {
                        builder.append(String.format(PRINT_FORMAT, name + " Params: ",
                                entry.getKey() + " = " + entry.getValue()));
                    }
                }
            }
            name = "Path";
            if (httpHybridParams.getPathParams().getParams().isEmpty()) {
                builder.append(String.format(PRINT_FORMAT, name + " Params: ", NONE));
            } else {
                int i = 0;
                for (Map.Entry<String, String> entry : httpHybridParams.getPathParams().getParams().entrySet()) {
                    if (i++ != 0) {
                        builder.append(String.format(PRINT_FORMAT, "", entry.getKey() + " = " + entry.getValue()))
                                .append("\n");
                    } else {
                        builder.append(String.format(PRINT_FORMAT, name + " Params: ",
                                entry.getKey() + " = " + entry.getValue()));
                    }
                }
            }
            name = "Form";
            if (httpHybridParams.getFormParams().getParams().isEmpty()) {
                builder.append(String.format(PRINT_FORMAT, name + " Params: ", NONE));
            } else {
                int i = 0;
                for (Map.Entry<String, String> entry : httpHybridParams.getFormParams().getParams().entrySet()) {
                    if (i++ != 0) {
                        builder.append(String.format(PRINT_FORMAT, "", entry.getKey() + " = " + entry.getValue()))
                                .append("\n");
                    } else {
                        builder.append(String.format(PRINT_FORMAT, name + " Params: ",
                                entry.getKey() + " = " + entry.getValue()));
                    }
                }
            }
        }
        return builder.toString();
    }

    /**
     * Append headers and params to {@link HttpRequest}
     *
     * @param request
     *            {@link HttpRequest}
     * @param spec
     *            {@link HTTPRequestSpecification}
     */

    private void appendHeadersAndParams(HttpRequest request, HTTPRequestSpecification spec) {
        if (spec == null || request == null) {
            return;
        }
        if (spec.getHeaders() != null && spec.getHeaders().getHeaders() != null) {
            spec.getHeaders().getHeaders().forEach((key, value) -> request.header(key, value));
        }
        if (spec.getParams() != null) {

            Params<?> params = spec.getParams();

            if (!params.getParams().isEmpty() && params instanceof HTTPParams) {
                if (request instanceof HttpRequestWithBody) {
                    HTTPParams formParams = (HTTPParams) spec.getParams();
                    HttpRequestWithBody requestWithBody = (HttpRequestWithBody) request;
                    formParams.getParams().forEach((key, value) -> requestWithBody.field(key, value));
                }
            } else if (!params.getParams().isEmpty() && params instanceof HTTPQueryParams) {
                HTTPQueryParams queryParams = (HTTPQueryParams) spec.getParams();
                if (!queryParams.getParams().isEmpty()) {
                    queryParams.getParams().forEach((key, value) -> request.queryString(key, value));
                }
            } else if (!params.getParams().isEmpty() && params instanceof HTTPPathParams) {
                HTTPPathParams pathParams = (HTTPPathParams) spec.getParams();
                if (!pathParams.getParams().isEmpty()) {
                    pathParams.getParams().forEach((key, value) ->
                            request.routeParam(key, value));
                }
            } else if (params instanceof HTTPHybridParams) {
                HTTPHybridParams hybridParams = (HTTPHybridParams) spec.getParams();
                if (!hybridParams.getQueryParams().getParams().isEmpty()) {
                    hybridParams.getQueryParams().getParams().forEach((key, value) -> request.queryString(key, value));
                }
                if (!hybridParams.getPathParams().getParams().isEmpty()) {
                    hybridParams.getPathParams().getParams().forEach((key, value) -> request.routeParam(key, value));
                }
                if (request instanceof HttpRequestWithBody && !hybridParams.getFormParams().getParams().isEmpty()) {
                    HttpRequestWithBody requestWithBody = (HttpRequestWithBody) request;
                    hybridParams.getFormParams().getParams().forEach((key, value) -> requestWithBody.field(key, value));
                }
            }
        }
    }

    /**
     * Get the body for POST, PUT, DELETE, .. calls
     *
     * @param specification
     *            {@link HTTPRequestSpecification}
     * @return {@link String}
     */
    private String getBody(HTTPRequestSpecification specification) {
        Object requestBody = specification.getBody();
        if (requestBody == null) {
            return null;
        }
        if (requestBody instanceof String) {
            return (String) requestBody;
        }
        return GSON.toJson(requestBody);
    }



    @Override
    public String getBaseUrl() {
        return null;
    }

    @Override
    public void createRequest(String serviceName, HTTPRequestSpecification httpRequestSpecification, String endPoint, String protocol) {
        this.uri = serviceName+endPoint;
        this.specification = httpRequestSpecification;
        this.baseUrl = serviceName+endPoint;
    }

    @Override
    public HTTPResponseHandler get() {
        return null;
    }

    @Override
    public HTTPResponseHandler post() {
        return null;
    }

    @Override
    public HTTPResponseHandler put() {
        return null;
    }

    @Override
    public HTTPResponseHandler delete() {
        return null;
    }

    @Override
    public HTTPResponseHandler head() {
        return null;
    }

    @Override
    public HTTPResponseHandler options() {
        return null;
    }

    @Override
    public HTTPResponseHandler patch() {
        return null;
    }

    @Override
    public HTTPResponseHandler uploadFiles() {
        return null;
    }

    @Override
    public HTTPResponseHandler downloadFiles() {
        return null;
    }
}
