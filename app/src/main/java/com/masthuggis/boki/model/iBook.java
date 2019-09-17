package com.masthuggis.boki.model;

import java.util.Collection;

public interface iBook {

    String getTitle();
    String getAuthor();
    int getEdition();
    long getIsbn();
    int getYearPublished();
    Book.Condition getCondition();
    Collection<String> getPreDefinedTags();
    Collection<String> getUserTags();
    int getPrice();
}
