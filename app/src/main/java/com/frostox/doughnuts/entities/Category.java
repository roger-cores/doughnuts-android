package com.frostox.doughnuts.entities;

/**
 * Created by roger on 11/1/2016.
 */
public class Category {

    private String name;

    private String key;

    public Category() {
    }

    public Category(String name, String imageUrl) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
