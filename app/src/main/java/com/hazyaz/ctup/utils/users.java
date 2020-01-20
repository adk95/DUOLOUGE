package com.hazyaz.ctup.utils;

public class users {

    public String name;
    public String image;
    public String status;
    public String thumb_image;



    public users()
    {

    }

    public users(String name,String image,String thumb_image,String status)
    {
        this.name = name;
        this.image =image;
        this.thumb_image = thumb_image;
        this.status = status;
    }

    public String getThumbImage() {
        return thumb_image;
    }

    public void setThumbImage(String thumbImage){this.thumb_image = thumbImage;}

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setStatus(String status) {
        this.status = status;
    }
}