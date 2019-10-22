package com.masthuggis.boki;


import com.masthuggis.boki.model.Advertisement;
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
        adverts = MockRepository.getTestAds();
    }

    private void sort(SortStrategy strategy) {
        adverts = strategy.sort(adverts);
    }

    @Test
    public void testAlphabeticalSortingIsCorrect() {
        sort(SortFactory.getAlphabeticalSorting());

        assertEquals(adverts.get(0).getTitle().toLowerCase(), "arkitektur");
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
        assertEquals(adverts.get(5).getTitle().toLowerCase(), "arkitektur");
    }

    @Test
    public void testHighestPriceSortingIsCorrect() {
        sort(SortFactory.getHighestPriceSorting());

        assertEquals(Long.toString(adverts.get(0).getPrice()), "450");
        assertEquals(Long.toString(adverts.get(adverts.size()-1).getPrice()), "180");
    }

    @Test
    public void testLowestPriceSortingIsCorrect() {
        sort(SortFactory.getLowestPriceSorting());

        assertEquals(Long.toString(adverts.get(0).getPrice()), "180");
        assertEquals(Long.toString(adverts.get(adverts.size()-1).getPrice()), "450");
    }

    @Test
    public void testLatestPublishedSortingIsCorrect() {
        sort(SortFactory.getLatestPublishedSorting());

        assertEquals(adverts.get(0).getTitle().toLowerCase(), "clean code");
        assertEquals(adverts.get(1).getTitle().toLowerCase(), "design");
        assertEquals(adverts.get(2).getTitle().toLowerCase(), "arkitektur");
        assertEquals(adverts.get(3).getTitle().toLowerCase(), "linjär algebra");
        assertEquals(adverts.get(4).getTitle().toLowerCase(), "calculus");
        assertEquals(adverts.get(5).getTitle().toLowerCase(), "diskret");
    }
}
