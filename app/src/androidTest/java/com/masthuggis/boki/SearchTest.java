package com.masthuggis.boki;


import androidx.test.runner.AndroidJUnit4;

import com.masthuggis.boki.backend.MockRepository;
import com.masthuggis.boki.backend.PerformedSearchCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.SearchHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SearchTest {
    List<Advertisement> ads = new ArrayList<>();

    @Before
    public void before() {
        ads = MockRepository.getInstance().getLocalJSONAds();
    }

    @Test
    public void testGetMatchedTitleCallback() {
        String query = "Calculus";
        SearchHelper.mockSearch(query, new PerformedSearchCallback() {
            @Override
            public void onCallback(List<Advertisement> searchRes) {
                assertTrue(searchRes.size() == 1);
                assertTrue(searchRes.get(0).getTitle().equals("Calculus"));
            }
        });

    }


    @Test
    public void testGetCharTitleCallback() {
        String query = "C";
        SearchHelper.mockSearch(query, new PerformedSearchCallback() {
            @Override
            public void onCallback(List<Advertisement> searchRes) {
                assertTrue(searchRes.size() == 2);
            }
        });

    }
    @Test
    public void testGetTitleContains() {
        String query = "A";
        SearchHelper.mockSearch(query, new PerformedSearchCallback() {
            @Override
            public void onCallback(List<Advertisement> searchRes) {
                System.out.println("SEARCHRESSIZE!!!"+searchRes.size());
                assertTrue(searchRes.size() == 3);
            }
        });

    }
    //TODO: Rewrite mockrepository to test searchPerformed with tags



}
