package com.masthuggis.boki.utils.sorting;

import com.masthuggis.boki.backend.MockRepository;
import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SortingTest {
    private List<Advertisement> adverts = new ArrayList<>();


    {
        "books": [
        {
            "title": "Diskret",
                "author": "Me",
                "edition": 1,
                "isbn": 0,
                "yearPublished": 2019,
                "price": 180,
                "condition": "GOOD",
                "date": "01/01/18 10:12:55"
        }, {
        "title": "Calculus",
                "author": "A.ADAMS",
                "edition": 8,
                "isbn" : 0,
                "yearPublished" : 2017,
                "price" : 450,
                "condition" : "OK",
                "date": "01/11/18 10:12:55"
    }, {
        "title" : "Linjär Algebra",
                "author" : "Stefan Lemurell",
                "edition" : 1,
                "isbn" : 0,
                "yearPublished" : 2016,
                "price" : 250,
                "condition" : "GOOD",
                "date": "01/12/18 10:12:55"
    }, {
        "title" : "Arikitektur",
                "author" : "Stefan Lemurell",
                "edition" : 1,
                "isbn" : 0,
                "yearPublished" : 2016,
                "price" : 300,
                "condition" : "GOOD",
                "date": "01/01/19 10:12:55"
    }, {
        "title" : "Design",
                "author" : "Stefan Lemurell",
                "edition" : 1,
                "isbn" : 0,
                "yearPublished" : 2016,
                "price" : 250,
                "condition" : "OK",
                "date": "02/01/19 10:12:55",
                "userTags" : [
        "Cool"
        ]
    }, {
        "title" : "Clean Code",
                "author" : "Robert C. Martin",
                "edition" : 1,
                "isbn" : "9780132350884",
                "yearPublished" : 2008,
                "price" : 200,
                "condition" : "NEW",
                "date": "03/01/19 08:12:55",
                "preDefinedTags" : [
        "IT/Data"
      ],
        "userTags" : [
        "Programmering", "Objektorientering", "Software Engineering", "Programvaruteknik"
      ]
    }
  ]
    }

    @Before
    public void before() {
        String date = "01/01/18 10:12:55";

        adverts = new ArrayList<>(
                Arrays.asList(
                        AdFactory.createAd(date, "user1", "id1", "Diskret", )
                )
        );
        adverts = MockRepository.getInstance().getLocalJSONAds();
    }

    private void sort(SortStrategy strategy) {
        adverts = strategy.sort(adverts);
    }

    @Test
    public void testAlphabeticalSortingIsCorrect() {
        sort(SortFactory.getAlphabeticalSorting());

        assertEquals(adverts.get(0).getTitle().toLowerCase(), "arikitektur");
        assertEquals(adverts.get(1).getTitle().toLowerCase(), "calculus");
        assertEquals(adverts.get(2).getTitle().toLowerCase(), "clean code");
        assertEquals(adverts.get(3).getTitle().toLowerCase(), "design");
        assertEquals(adverts.get(4).getTitle().toLowerCase(), "diskret");
        assertEquals(adverts.get(5).getTitle().toLowerCase(), "linjär algebra");
    }

    @Test
    public void testReverseAlphabeticalSortingIsCorrect() {
        sort(SortFactory.getReversedAlphabeticalSorting());

        assertEquals(adverts.get(0).getTitle().toLowerCase(), "linjär algebra");
        assertEquals(adverts.get(1).getTitle().toLowerCase(), "diskret");
        assertEquals(adverts.get(2).getTitle().toLowerCase(), "design");
        assertEquals(adverts.get(3).getTitle().toLowerCase(), "clean code");
        assertEquals(adverts.get(4).getTitle().toLowerCase(), "calculus");
        assertEquals(adverts.get(5).getTitle().toLowerCase(), "arikitektur");
    }

    @Test
    public void testHighestPriceSortingIsCorrect() {
        sort(SortFactory.getHighestPriceSorting());

        assertEquals(Long.toString(adverts.get(0).getPrice()), "450");
        assertEquals(Long.toString(adverts.get(adverts.size() - 1).getPrice()), "180");
    }

    @Test
    public void testLowestPriceSortingIsCorrect() {
        sort(SortFactory.getLowestPriceSorting());

        assertEquals(Long.toString(adverts.get(0).getPrice()), "180");
        assertEquals(Long.toString(adverts.get(adverts.size() - 1).getPrice()), "450");
    }

    @Test
    public void testLatestPublishedSortingIsCorrect() {
        sort(SortFactory.getLatestPublishedSorting());

        assertEquals(adverts.get(0).getTitle().toLowerCase(), "clean code");
        assertEquals(adverts.get(1).getTitle().toLowerCase(), "design");
        assertEquals(adverts.get(2).getTitle().toLowerCase(), "arikitektur");
        assertEquals(adverts.get(3).getTitle().toLowerCase(), "linjär algebra");
        assertEquals(adverts.get(4).getTitle().toLowerCase(), "calculus");
        assertEquals(adverts.get(5).getTitle().toLowerCase(), "diskret");
    }
}