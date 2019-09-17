package com.masthuggis.boki.model;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents an Advert.
 * Only required fields are a User object and a date when advert was created.
 * All other information gathered from object being sold.
 */
public class Advert implements Advertisement {
    private int userId;
    private String datePublished;
    private iBook bookForSale;
    private String name;
    private URL imgUrl;


    public Advert(String datePublished, String name, URL imgUrl, int id) {
        this.userId = userId;
        this.datePublished = datePublished;
        this.name = name;
        this.imgUrl = imgUrl;
        this.userId = id;
    }

    public Advert(int userId,String datePublished,iBook book){
        this.userId = userId;
        this.datePublished=datePublished;
        this.bookForSale=book;
        this.name = book.getTitle();
        int i= book.getPrice();
    }
    //Default Constructor
    public Advert() {
        this.datePublished = null;
        this.name = "Standard Advert";
        try {
            this.imgUrl = new URL("https://dsvmninsg3s3x.cloudfront.net/uploads/2017/08/0134154363.jpg");
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        this.userId = 0;
    }

    public Advert(String datePublished, String name, int price) {
        this.datePublished = datePublished;
        this.name = name;
    }


    public Advert(String datePublished, String name, int price, URL imgUrl) {
        this.datePublished = datePublished;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getDatePublished() {
        return this.datePublished;
    }

    @Override
    public URL getImgURL() {
        return this.imgUrl;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getPrice() {
        return this.bookForSale.getPrice();
    }

    public iBook getBookForSale() {
        return this.bookForSale;
    }


}
