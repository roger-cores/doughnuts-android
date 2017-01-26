package com.frostox.doughnuts.entities;

/**
 * Created by roger on 11/1/2016.
 */
public class Recipe {

    private String key;

    private String name;

    private String authorUUID;

    private String category;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorUUID() {
        return authorUUID;
    }

    public void setAuthorUUID(String authorUUID) {
        this.authorUUID = authorUUID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
