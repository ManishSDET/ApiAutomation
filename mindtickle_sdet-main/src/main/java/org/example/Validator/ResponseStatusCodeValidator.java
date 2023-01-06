package org.example.Validator;

import junit.framework.Assert;
import org.example.framework.HTTPStatus;

public class ResponseStatusCodeValidator implements ResponseValidate{

    public void validatingResponseStatusCode(HTTPStatus httpStatus, int expectedCode, String expectedStatusLine){
        Assert.assertEquals(expectedCode, httpStatus.getStatusCode()); // "Expected status code : " + expectedCode + "But found :" + httpStatus.getStatusCode());
    }
}
