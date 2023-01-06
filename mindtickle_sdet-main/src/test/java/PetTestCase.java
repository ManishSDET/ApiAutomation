import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Helper.PetHelper;
import org.example.Models.RequestModels.Pet;
import org.example.Models.ResponseModels.Pets;
import org.example.Validator.ResponseBodyValidator;
import org.example.Validator.ResponseStatusCodeValidator;
import org.example.framework.HTTPResponseHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PetTestCase {

    private PetHelper petHelper = new PetHelper();

    private ResponseStatusCodeValidator responseStatusValidator = new ResponseStatusCodeValidator();

    private ResponseBodyValidator responseBodyValidator = new ResponseBodyValidator();

    private ObjectMapper obj = new ObjectMapper();

    @Test
    public void createPet() {
        HTTPResponseHandler httpResponse = petHelper.createPet();
        responseStatusValidator.validatingResponseStatusCode(httpResponse.getStatusLine(), 200, "successful operation");
    }

    @Test
    public void createDuplicatePet() {

        HTTPResponseHandler httpResponse = petHelper.createDuplicateDP();
        responseStatusValidator.validatingResponseStatusCode(httpResponse.getStatusLine(), 200, "successful operation");

    }

    @Test
    public void updateStatusInPet() {
        HTTPResponseHandler httpResponse = petHelper.updatePet();
        responseStatusValidator.validatingResponseStatusCode(httpResponse.getStatusLine(), 200, "successful operation");

    }

    @Test
    public void getByAvailableStatus() throws JsonProcessingException {
        HTTPResponseHandler httpResponseHandler = petHelper.getPetByStatus("available");
        responseStatusValidator.validatingResponseStatusCode(httpResponseHandler.getStatusLine(), 200, "successful operation");
        Pets pet = obj.readValue(httpResponseHandler.getResponseAsString(), Pets.class);
        List<Pet> pets = pet.getPets();
        if (pets.isEmpty())
            System.out.println("No Sold pets are sold out");
        for (Pet a : pets) {
            Assert.assertEquals(a.getStatus(), "sold");
        }
    }


}
