package org.example.Validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Assert;
import org.example.Models.ResponseModels.ResponseBody;
import org.example.framework.HTTPBody;

public class ResponseBodyValidator {

    ObjectMapper objectMapper = new ObjectMapper();
    public void validateResponseBody(HTTPBody httpBody) throws JsonProcessingException {
        ResponseBody responseBody = objectMapper.readValue(httpBody.getBodyText(), ResponseBody.class);
        Assert.assertEquals("ok", responseBody.getMessage());
    }
}
