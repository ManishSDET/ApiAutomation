package org.example.framework;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HTTPRequestSpecification {

    private HTTPHeader HttpHeader;

    private Params<?> params;

    private Object body;

    private List<File> files;

    private boolean includeRootWrapper;

    public HTTPRequestSpecification(){
    }

    public HTTPRequestSpecification(HTTPHeader HttpHeader){
        this.HttpHeader=HttpHeader;
    }

    public HTTPRequestSpecification(Params<?> params){
        this.params=params;
    }

    public HTTPRequestSpecification(HTTPHeader HttpHeader, Params<?> params){
        this.HttpHeader=HttpHeader;
        this.params=params;
    }

    public HTTPRequestSpecification(HTTPHeader HttpHeader, Params<?> params, Object body){
        this(HttpHeader, params, body, true);
    }

    public HTTPRequestSpecification(HTTPHeader HttpHeader, Params<?> params, Object body, boolean includeRootWrapper) {
        this.HttpHeader=HttpHeader;
        this.params=params;
        this.body=body;
        this.includeRootWrapper=includeRootWrapper;
    }

    public HTTPRequestSpecification(HTTPHeader HttpHeader, Object body){
        this(HttpHeader, body, true);
    }

    public HTTPRequestSpecification(HTTPHeader HttpHeader, Object body, boolean includeRootWrapper){
        this.HttpHeader=HttpHeader;
        this.body=body;
        this.includeRootWrapper=includeRootWrapper;
    }

    public HTTPRequestSpecification(Params<?> params, Object body){
        this(params, body, true);
    }

    public HTTPRequestSpecification(Params<?> params, Object body, boolean includeRootWrapper) {
        this.params=params;
        this.body=body;
        this.includeRootWrapper=includeRootWrapper;
    }

    public HTTPRequestSpecification(Object body){
        this(body, true);
    }

    public HTTPRequestSpecification(Object body, boolean includeRootWrapper) {
        this.body=body;
        this.includeRootWrapper=true;
    }

    public HTTPHeader getHeaders(){ return HttpHeader;}

    public Params<?> getParams(){ return params;}

    public Object getBody(){ return body;}

    public boolean isIncludeRootWrapper(){ return includeRootWrapper;}

    public void setFile(File file){
        if(files == null)
            files = new ArrayList<>();
        files.add(file);
    }

    public void setFiles(List<File> files){ this.files = files;}

    public List<File> getFiles(){ return files;}

    @Override
    public String toString() {
        return "HTTPRequestSpecification [headers=" + ", body=" + body + ", completeServicePath="
                + ", params=" + params + ", files=" + files + "]";
    }
}