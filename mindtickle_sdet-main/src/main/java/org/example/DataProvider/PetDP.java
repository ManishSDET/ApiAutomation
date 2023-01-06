package org.example.DataProvider;

import org.example.Models.RequestModels.Category;
import org.example.Models.RequestModels.Pet;

import java.util.Random;

public class PetDP {
    Random rand = new Random();
    public Pet createPet(){
        Pet pet = new Pet();
        int id = rand.nextInt(10000);
        pet.setId(id);
        Category category = new Category();
        int categoryId = rand.nextInt(100);
        category.setId(categoryId);
        category.setName("category"+categoryId);
        pet.addPhotoURLs("");
        pet.setStatus("available");
        return pet;
    }

}
