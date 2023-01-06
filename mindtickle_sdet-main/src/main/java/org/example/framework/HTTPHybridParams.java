package org.example.framework;

public class HTTPHybridParams extends Params<HTTPHybridParams>{

    private HTTPPathParams pathParams = new HTTPPathParams();

    private HTTPQueryParams queryParams = new HTTPQueryParams();

    private HTTPParams formParams = new HTTPParams();

    public HTTPPathParams getPathParams() { return this.pathParams;}

    public void setPathParams(HTTPPathParams pathParams) { this.pathParams = pathParams;}

    public HTTPQueryParams getQueryParams() { return this.queryParams;}

    public void setQueryParams(HTTPQueryParams queryParams) { this.queryParams = queryParams;}

    public HTTPParams getFormParams() { return this.formParams;}

    public void setPathParams(HTTPParams formParams) { this.formParams = formParams;}


}
