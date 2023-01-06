package org.example.Models.ResponseModels;

import org.example.Models.RequestModels.Pet;

import java.util.ArrayList;
import java.util.List;

public class Pets {

    private List<Pet> pets = new ArrayList<>();

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
