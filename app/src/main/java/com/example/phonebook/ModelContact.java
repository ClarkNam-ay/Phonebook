package com.example.phonebook;

public class ModelContact {

    private String id,image,name,mobile_number;

    //create constructor

    public ModelContact(String id, String image, String name, String mobile_number) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.mobile_number = mobile_number;
    }

    //create getter and setter method


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
}
