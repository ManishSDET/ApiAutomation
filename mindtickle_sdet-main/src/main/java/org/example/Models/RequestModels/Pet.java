package org.example.Models.RequestModels;

import java.util.ArrayList;
import java.util.List;

public class Pet {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getPhotoURLs() {
        return photoURLs;
    }

    public void setPhotoURLs(List<String> photoURLs) {
        this.photoURLs = photoURLs;
    }

    public void addPhotoURLs(String url){
        this.photoURLs.add(url);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTags(Tag tag){
        this.tags.add(tag);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Category category;

    private List<String> photoURLs = new ArrayList<>();

    private List<Tag> tags;

    private String status;


}
