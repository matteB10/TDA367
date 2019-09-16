package com.masthuggis.boki.model;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents an Advert.
 * Only required fields are a User object and a date when advert was created.
 * All other information gathered from object being sold.
 */
public abstract class Advert {
    private User seller;
    private String datePublished;
    private String name;
    private URL imgUrl;
    private long id;
    private int price;

    public Advert(User seller, String datePublished, String name, URL imgUrl, long id, int price) {
        this.seller = seller;
        this.datePublished = datePublished;
        this.name = name;
        this.imgUrl = imgUrl;
        this.id = id;
        this.price = price;
    }

    //Default Constructor
    public Advert() {
        this.seller = null;
        this.datePublished = null;
        this.name = "Standard Advert";
        try {
            this.imgUrl = new URL("https://dsvmninsg3s3x.cloudfront.net/uploads/2017/08/0134154363.jpg");
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        this.id = 0;
        this.price = 0;
    }

    public Advert(String datePublished, String name, int price) {
        this.datePublished = datePublished;
        this.name = name;
        this.price = price;
    }


    public Advert(String datePublished, String name, int price, URL imgUrl) {
        this.datePublished = datePublished;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public User getSeller() {
        return this.seller;
    }

    public String getDatePublished() {
        return this.datePublished;
    }

    public String getName() {
        return name;
    }

    public URL getImgUrl() {
        return imgUrl;
    }

    public long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }
}
