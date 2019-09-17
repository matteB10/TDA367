package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Book;

import java.util.List;

public class AdFactory {

    public static Advertisement createAd(int UID, String datePublished,
                                         String title, String author, int edition,
                                         long isbn, int yearPublished, int price, Book.Condition condition,
                                         List<String> predefinedTags, List<String> userTags) {

        return new Advert(UID, datePublished, new Book(title, author, edition, price, isbn, yearPublished, condition, userTags, predefinedTags));
    }


}
