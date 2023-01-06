package org.example.framework;

import java.util.HashMap;
import java.util.Map;

public abstract class Params<T extends Params<T>> {

    private Map<String, String> params = new HashMap<>();

    public Map<String, String> getParams(){ return this.params;}

    public void setParams(Map<String, String> params){ this.params = params;}

    public void addParams(String key, String value){ this.params.put(key, value);}

    public String getParam(String key){ return this.params.get(key);}

    @Override
    public String toString(){ return "HTTP Params=[" + params + "]";}
}
