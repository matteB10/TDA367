package com.masthuggis.boki;

import androidx.test.runner.AndroidJUnit4;

import com.masthuggis.boki.backend.MockRepository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.sorting.SortFactory;
import com.masthuggis.boki.model.sorting.SortStrategy;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SortingTest {
    @Test
    public void testAlphabeticalSortingIsCorrect() {
        List<Advertisement> sorted = MockRepository.getInstance().getLocalJSONAds();
        SortStrategy strategy = SortFactory.getAlphabeticalSorting();

        sorted = strategy.sort(sorted);

        assertEquals(sorted.get(0).getTitle(), "Arikitektur");
        assertEquals(sorted.get(1).getTitle(), "Calculus");
    }
}
