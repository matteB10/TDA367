package com.masthuggis.boki;


import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.utils.sorting.SortFactory;
import com.masthuggis.boki.utils.sorting.SortStrategy;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SortingTest {
    private List<Advertisement> adverts = new ArrayList<>();

    @Before
    public void before() {
        adverts.add(AdFactory.createAd( "22/10/19:13:16:00", "UniqueOwnerID", "UniqueAdID", "ABC","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        adverts.add(AdFactory.createAd( "22/10/19:10:16:00", "UniqueOwnerID", "UniqueAdID", "QYZ","", 490, Condition.GOOD,"", new ArrayList<>(),null));
        adverts.add(AdFactory.createAd( "22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID", "DEF","", 299, Condition.GOOD,"", new ArrayList<>(),null));
    }

    private void sort(SortStrategy strategy) {
        adverts = strategy.sort(adverts);
    }

    @Test
    public void testAlphabeticalSortingIsCorrect() {
        sort(SortFactory.getAlphabeticalSorting());

        assertEquals(adverts.get(0).getTitle().toLowerCase(), "abc");
        assertEquals(adverts.get(1).getTitle().toLowerCase(), "def");
        assertEquals(adverts.get(2).getTitle().toLowerCase(), "qyz");
    }

    @Test
    public void testReverseAlphabeticalSortingIsCorrect() {
        sort(SortFactory.getReversedAlphabeticalSorting());

        assertEquals(adverts.get(0).getTitle().toLowerCase(), "qyz");
        assertEquals(adverts.get(1).getTitle().toLowerCase(), "def");
        assertEquals(adverts.get(2).getTitle().toLowerCase(), "abc");
    }

    @Test
    public void testHighestPriceSortingIsCorrect() {
        sort(SortFactory.getHighestPriceSorting());

        assertEquals(Long.toString(adverts.get(0).getPrice()), "490");
        assertEquals(Long.toString(adverts.get(adverts.size()-1).getPrice()), "299");
    }

    @Test
    public void testLowestPriceSortingIsCorrect() {
        sort(SortFactory.getLowestPriceSorting());

        assertEquals(Long.toString(adverts.get(0).getPrice()), "299");
        assertEquals(Long.toString(adverts.get(adverts.size()-1).getPrice()), "490");
    }

    @Test
    public void testLatestPublishedSortingIsCorrect() {
        sort(SortFactory.getLatestPublishedSorting());

        assertEquals(adverts.get(0).getTitle().toLowerCase(), "abc");
        assertEquals(adverts.get(1).getTitle().toLowerCase(), "qyz");
        assertEquals(adverts.get(2).getTitle().toLowerCase(), "def");
    }
}
