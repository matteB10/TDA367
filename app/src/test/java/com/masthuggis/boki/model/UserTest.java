package com.masthuggis.boki.model;

import com.masthuggis.boki.utils.Condition;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

public class UserTest {

    @Mock
    private DataModel databaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    Advertisement advertisement = new Advert("datePublished", "uniqueOwnerID", "id", "title", "description", 123123, Condition.NEW, "imageURL", new ArrayList<>(), "owner");


    @Test
    public void testAddAdvertToFavourites() {
        User user = new User("email", "displayName", "userID");
        assert (user.getFavourites().size() == 0);
        user.addFavourite(advertisement);
        assert (user.getFavourites().size() == 1);
    }

    @Test
    public void testRemoveAdvertFromFavourites() {
        User user = new User("email", "displayName", "userID");
        user.addFavourite(advertisement);
        assert (user.getFavourites().size() == 1);
        user.removeFavourite(advertisement);
        assert (user.getFavourites().size() == 0);
    }

    @Test
    public void testSetFavourites() {
        List<Advertisement> advertisements = new ArrayList<>();
        advertisements.add(advertisement);

        User user = new User("email", "displayName", "userID");
        assert (user.getFavourites().size() == 0);
        user.setFavourites(advertisements);
        assert (user.getFavourites().size() == 1);
        advertisements.add(advertisement);
        assert (user.getFavourites().size() == 2);
    }

    @Test
    public void testGetFavourites() {
        List<Advertisement> advertisements = new ArrayList<>();
        User user = new User("email", "displayName", "userID");
        assert (user.getFavourites() != advertisements);
        user.setFavourites(advertisements);
        assert (user.getFavourites() == advertisements);
    }

}
