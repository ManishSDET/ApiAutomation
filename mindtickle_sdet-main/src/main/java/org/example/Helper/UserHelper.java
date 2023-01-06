package org.example.Helper;

import org.example.DataProvider.UserDP;
import org.example.Models.RequestModels.User;
import org.example.constants.ServiceName;
import org.example.constants.UrlConstants;
import org.example.framework.*;
import org.example.utils.test_utils.APIUtil;
import org.example.utils.test_utils.HeaderUtil;

import java.util.List;

public class UserHelper {

    UserDP userDP = new UserDP();
    APIUtil api = new APIUtil();

    HeaderUtil header = new HeaderUtil();

    public HTTPRequestSpecification requestSpecification;

    private static final String baseURL = ServiceName.SERVICE_NAME;

    private HTTPHeader httpHeader = header.createHeaderForJSON();

    public HTTPResponseHandler createUserWithArray(int n){
        List<User> users = userDP.createMultipleUsersWithArray(n);
        requestSpecification = new HTTPRequestSpecification(httpHeader, null, users);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.POST,
                requestSpecification, UrlConstants.USER_CREATE_WITH_ARRAY, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler;
    }

    public HTTPResponseHandler getUserDetails(String userName){
        HTTPPathParams httpPathParams = new HTTPPathParams();
        httpPathParams.addParams("username", userName);
        requestSpecification = new HTTPRequestSpecification(httpHeader, httpPathParams, null);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.GET,
                requestSpecification, UrlConstants.GET_USER_DETAIL, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler;
    }

    public HTTPResponseHandler updateUser(){
        User user = userDP.createUser();
        createUser(user);
        HTTPPathParams httpPathParams = new HTTPPathParams();
        httpPathParams.addParams("username", user.getUserName());
        User updateUser = new User();
        updateUser.setFirstName("Maniya");
        updateUser.setPhone("9988998899");
        updateUser.setUserName("manish3");
        requestSpecification = new HTTPRequestSpecification(httpHeader, httpPathParams, updateUser);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.PUT,
                requestSpecification, UrlConstants.UPDATE_USER, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler;
    }

    public HTTPResponseHandler createDuplicateUser(){
        List<User> users = userDP.createDuplicateMultipleUser();
        requestSpecification = new HTTPRequestSpecification(httpHeader, null, users);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.POST,
                requestSpecification, UrlConstants.USER_CREATE_WITH_ARRAY, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler;
    }

    public HTTPResponseHandler createUser(User user){
        requestSpecification = new HTTPRequestSpecification(httpHeader, null, user);
        HTTPResponseHandler httpResponseHandler = api.createRequest(baseURL, HTTPMethod.POST,
                requestSpecification, UrlConstants.CREATE_USER, ServiceName.PROTOCOL_NAME);
        return httpResponseHandler;
    }
}
