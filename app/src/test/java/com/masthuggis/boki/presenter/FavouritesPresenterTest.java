package com.masthuggis.boki.presenter;

import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.ThumbnailView;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class FavouritesPresenterTest {

    FavouritesPresenter presenter = new FavouritesPresenter(null, DependencyInjector.injectDataModel()); //TODO create mock View, inject

    @Mock
    private DataModel databaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    Advertisement advertisement = new Advert(
            "datePublished",
            "uniqueOwnerID",
            "id",
            "title",
            "description",
            123123,
            Advert.Condition.NEW,
            null,
            new ArrayList<>(),
            "owner");

    ThumbnailView thumbnailView = new ThumbnailView() {
        @Override
        public void setTitle(String name) {
            advertisement.setTitle(name);
        }

        @Override
        public void setPrice(long price) {
            advertisement.setPrice((int)price);
        }

        @Override
        public void setImageURL(String url) {

        }

        @Override
        public void setId(String id) {

        }

        @Override
        public void setCondition(int condition, int color) {
            advertisement.setCondition(condition);
        }
    };


    @Test
    public void testGetItemCount() {

    }

    @Test
    public void testOnBindThumbnailViewAtPosition() {

    }
}
