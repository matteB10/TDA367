package com.masthuggis.boki.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Book with common administrative information.
 */

public class Book extends Advert {
    public enum Condition {
        NEW, GOOD, OK;
    }

    private String title;
    private String author;
    private int edition;
    private long isbn;
    private int yearPublished;
    private Condition condition;
    private final List<String> preDefinedTags;
    private List<String> userTags;

    /**
     * Constructor for creating book with information provided by the user.
     *
     * @param title         Title of the Book. Required information.
     * @param author        Author of the Book, could be more than one. Required information.
     * @param edition       The edition of the book, used to clarify what book is being sold to users. Required information.
     * @param isbn          The isb-number for the book.
     * @param yearPublished What year the book was published.
     * @param condition     Condition of the book, NEW, GOOD or OK.
     * @param userTags      User-defined tags for the book, searchable by other users.
     * @throws IllegalArgumentException if price is negative
     */

    public Book(User seller, String datePublished, String name, URL imgUrl, long id, int price, String title, String author, int edition, long isbn, int yearPublished, Condition condition, List<String> preDefinedTags, List<String> userTags) {
        super(seller, datePublished, name, imgUrl, id, price);
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.isbn = isbn;
        this.yearPublished = yearPublished;
        this.condition = condition;
        this.preDefinedTags = preDefinedTags;
        this.userTags = userTags;
    }

    public Book(String datePublished, String title, int price, String author, int edition, long isbn, Condition condition, List<String> preDefinedTags, List<String> userTags) {
        super(datePublished, title, price);
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.isbn = isbn;
        this.condition = condition;
        this.preDefinedTags = preDefinedTags;
        this.userTags = userTags;
    }

    /**
     * Method for accessing all tags, default ones as well as user-defined.
     *
     * @return a list with all book tags.
     */
    public List<String> getAllTags() {
        List<String> tagList = new ArrayList<>();
        tagList.addAll(preDefinedTags);
        tagList.addAll(userTags);
        return tagList;
    }

    public String getTitle() {
        return this.title;
    }


    public String getAuthor() {
        return this.author;
    }

    public int getEdition() {
        return edition;
    }

    public long getIsbn() {
        return isbn;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public List<String> getPreDefinedTags() {
        return this.preDefinedTags;
    }

    public List<String> getUserTags() {
        return userTags;
    }
}
