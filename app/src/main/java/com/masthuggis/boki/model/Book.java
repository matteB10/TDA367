package com.masthuggis.boki.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Book with common administrative information.
 */

public class Book {
    private enum Condition {
        NEW, GOOD, OK;
    }

    private String author;
    private int edition;
    private long isbn;
    private int yearPublished;
    private int price;
    private Condition condition;
    private final List<String> preDefinedTags;
    private List<String> userTags;

    /**
     * Constructor for creating book with information provided by the user.
     *
     * @param author        Author of the Book, could be more than one. Required information.
     * @param edition       The edition of the book, used to clarify what book is being sold to users. Required information.
     * @param price         The asking price for the book. Required information.
     * @param isbn          The isb-number for the book.
     * @param yearPublished What year the book was published.
     * @param condition     Condition of the book, NEW, GOOD or OK.
     * @param userTags      User-defined tags for the book, searchable by other users.
     * @throws IllegalArgumentException if price is negative
     */
    public Book(String author, int edition, int price, long isbn, int yearPublished, Condition condition,
                List<String> userTags, List<String> preDefinedTags) {
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative");
        this.author = author;
        this.yearPublished = yearPublished; //TODO add way of verifying that yearPublished is not in the future
        this.isbn = isbn;
        this.edition = edition;
        this.price = price;
        this.condition = condition;
        this.userTags = userTags;
        this.preDefinedTags = preDefinedTags;
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

    public int getPrice() {
        return this.price;
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
