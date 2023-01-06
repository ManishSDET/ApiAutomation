import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Helper.UserHelper;
import org.example.Models.ResponseModels.ResponseBody;
import org.example.Validator.ResponseBodyValidator;
import org.example.Validator.ResponseStatusCodeValidator;
import org.example.framework.HTTPResponseHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserTestCase {

    private UserHelper userHelper = new UserHelper();

    private ResponseStatusCodeValidator responseStatusValidator = new ResponseStatusCodeValidator();

    private ResponseBodyValidator responseBodyValidator = new ResponseBodyValidator();

    private ObjectMapper obj = new ObjectMapper();


    @Test
    public void getUserBeforeCreatingUser() throws JsonProcessingException {
        HTTPResponseHandler httpResponse = userHelper.getUserDetails("mindtickle");
        ResponseBody responseBody = obj.readValue(httpResponse.getResponse().getBody().getBodyText(), ResponseBody.class);
        Assert.assertEquals(responseBody.getCode(), 1, "Response Code Expected : 1. Actual :" + responseBody.getCode());
        Assert.assertEquals(responseBody.getMessage(), "User not found", "Response Message Expected : User not found. Actual :" + responseBody.getMessage());
        Assert.assertEquals(responseBody.getType(), "error","Response Type Expected : error. Actual :" + responseBody.getType());
    }

    @Test
    public void createUserWithEmptyArray() throws JsonProcessingException {
        HTTPResponseHandler httpResponse = userHelper.createUserWithArray(0);
        responseStatusValidator.validatingResponseStatusCode(httpResponse.getStatusLine(), 200, "successful operation");
        responseBodyValidator.validateResponseBody(httpResponse.getResponse().getBody());
    }

    @Test
    public void createSingleUserWithArray() throws JsonProcessingException {
        HTTPResponseHandler httpResponse = userHelper.createUserWithArray(1);
        responseStatusValidator.validatingResponseStatusCode(httpResponse.getStatusLine(), 200, "successful operation");
        responseBodyValidator.validateResponseBody(httpResponse.getResponse().getBody());
    }

    @Test
    public void createMultipleUserWithArray() throws JsonProcessingException {
        HTTPResponseHandler httpResponse = userHelper.createUserWithArray(2);
        responseStatusValidator.validatingResponseStatusCode(httpResponse.getStatusLine(), 200, "successful operation");
        responseBodyValidator.validateResponseBody(httpResponse.getResponse().getBody());
    }

    @Test
    public void getCreatedUser() throws JsonProcessingException {
        HTTPResponseHandler httpResponseHandler = userHelper.getUserDetails("manish3");
        responseStatusValidator.validatingResponseStatusCode(httpResponseHandler.getStatusLine(), 200, "successful operation");

    }

    @Test
    public void createDuplicateMultipleArray() throws JsonProcessingException{
        HTTPResponseHandler httpResponse = userHelper.createDuplicateUser();
        responseStatusValidator.validatingResponseStatusCode(httpResponse.getStatusLine(), 200, "successful operation");
        responseBodyValidator.validateResponseBody(httpResponse.getResponse().getBody());
    }

    @Test
    public void updateUserDetail() throws JsonProcessingException{
        HTTPResponseHandler httpResponse = userHelper.updateUser();
        responseStatusValidator.validatingResponseStatusCode(httpResponse.getStatusLine(), 200, "successful operation");
    }
}
