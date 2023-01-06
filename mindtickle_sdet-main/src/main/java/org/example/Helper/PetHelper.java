package org.example.Helper;

import org.example.DataProvider.PetDP;
import org.example.Models.RequestModels.Pet;
import org.example.constants.ServiceName;
import org.example.constants.UrlConstants;
import org.example.framework.*;
import org.example.utils.test_utils.APIUtil;
import org.example.utils.test_utils.HeaderUtil;

public class PetHelper {

    PetDP petDP = new PetDP();

    APIUtil api = new APIUtil();

    HeaderUtil header = new HeaderUtil();

    public HTTPRequestSpecification requestSpecification;

    private static final String baseURL = ServiceName.SERVICE_NAME;

    private HTTPHeader httpHeader = header.createHeaderForJSON();

    public HTTPResponseHandler createPet(){
        Pet pet = petDP.createPet();
        requestSpecification = new HTTPRequestSpecification(httpHeader, null, pet);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.POST,
                requestSpecification, UrlConstants.CREATE_PET, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler;
    }

    public HTTPResponseHandler createDuplicateDP(){
        Pet pet = petDP.createPet();
        requestSpecification = new HTTPRequestSpecification(httpHeader, null, pet);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.POST,
                requestSpecification, UrlConstants.CREATE_PET, ServiceName.PROTOCOL_NAME);
        HTTPResponseHandler httpResponseHandler1 = api.createRequest(baseURL, HTTPMethod.POST,
                requestSpecification, UrlConstants.CREATE_PET, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler1;
    }

    public HTTPResponseHandler updatePet(){
        Pet pet = petDP.createPet();
        requestSpecification = new HTTPRequestSpecification(httpHeader, null, pet);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.POST,
                requestSpecification, UrlConstants.CREATE_PET, ServiceName.PROTOCOL_NAME);
        pet.setStatus("pending");
        HTTPResponseHandler httpResponseHandler1 = api.createRequest(baseURL, HTTPMethod.PUT,
                requestSpecification, UrlConstants.UPDATE_PET, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler1;
    }

    public HTTPResponseHandler getPetByStatus(String status){
        HTTPQueryParams httpQueryParams = new HTTPQueryParams();
        httpQueryParams.addParams("status", status);
        requestSpecification = new HTTPRequestSpecification(httpHeader, httpQueryParams, null);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.GET,
                requestSpecification, UrlConstants.GET_PET_BY_STATUS, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler;
    }
}
