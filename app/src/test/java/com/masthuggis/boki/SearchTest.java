package com.masthuggis.boki;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.utils.SearchHelper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchTest {
    List<Advertisement> ads = new ArrayList<>();
    List<Advertisement> searchRes;

    @Before
    public void before() {
        searchRes = new ArrayList<>();

        ads.add(AdFactory.createAd( "22/10/19:13:16:00", "UniqueOwnerID", "UniqueAdID", "Kemi","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        ads.add(AdFactory.createAd( "22/10/19:10:16:00", "UniqueOwnerID", "UniqueAdID", "Calculus","", 490, Condition.GOOD,"", new ArrayList<>(),null));
        ads.add(AdFactory.createAd( "22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID", "Diskret matematik","", 299, Condition.GOOD,"", new ArrayList<>(),null));
        ads.add(AdFactory.createAd( "22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID", "Clean code","", 199, Condition.GOOD,"", new ArrayList<>(),null));
        ads.add(AdFactory.createAd( "22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID", "Linjär algebra","", 400, Condition.GOOD,"", new ArrayList<>(),null));
    }

    /**
     * Test if getActive searsch/filter behave as it should.
     * needs to be tested first
     */
    @Test
    public void testGetActive(){
        SearchHelper.search("",ads);
        assertEquals(false,SearchHelper.isActiveSearch());
        SearchHelper.search("kemi",ads);
        assertEquals(true,SearchHelper.isActiveSearch());
        SearchHelper.filters(200,new ArrayList<>(),ads);
        assertEquals(true,SearchHelper.isActiveFilters());
    }

    /**
     * Test if a known book is added to searchRes when searched
     */
    @Test
    public void testGetMatchedTitle() {
        String query = "Calculus";
        searchRes = SearchHelper.search(query,ads);
        assertEquals(1, searchRes.size());
    }

    /**
     * Test if a known book is added to searchRes when searched,
     * should not be case sensitive
     */
    @Test
    public void testGetMatchedTitleUpperCase() {
        String query = "CALCULUS";
        searchRes = SearchHelper.search(query,ads);
        assertEquals(1,searchRes.size());
    }


    /**
     * Only books that starts with the query letter should be returned from search.
     * If title contains the letter it should not be added to seearchRes.
     */

    @Test
    public void testGetMatchLetter() {
        String query = "C";
        searchRes = SearchHelper.search(query,ads);
        assertEquals(2, searchRes.size());
    }

    /**
     * If a book title or tag contains a query with length > 3,
     * it should be added to searchRes
     */
    @Test
    public void testGetTitleContains() {
        String query = "CODE";
        searchRes = SearchHelper.search(query,ads);
        assertEquals(1,searchRes.size());
    }

    /**
     * Test if price filters out all books with higher price
     * than input max price
     */
    @Test
    public void testFilterPriceOnly(){
        int maxPrice = 15;
        searchRes = SearchHelper.filters(maxPrice,new ArrayList<>(),ads);
        assertEquals(0,searchRes.size());
    }

    /**
     * Test if filtering with subject filters
     * and case sensitivity works
     */
    @Test
    public void testFilterTags(){
        List<String> filters = new ArrayList<>();
        filters.add("code");
        filters.add("algebra");
        filters.add("matematik");
        searchRes = SearchHelper.filters(500,filters,ads);
        assertEquals(3,searchRes.size());
        assertEquals("Clean code",searchRes.get(0).getTitle());
        assertEquals("Linjär algebra", searchRes.get(1).getTitle());
        assertEquals("Diskret matematik",searchRes.get(2).getTitle());
    }



}
