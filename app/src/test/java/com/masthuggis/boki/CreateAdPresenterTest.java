package com.masthuggis.boki;

import android.view.View;

import com.masthuggis.boki.presenter.CreateAdPresenter;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CreateAdPresenterTest {

    class MockView implements CreateAdPresenter.View{
        public void aMethod(){

        }
    }


    @BeforeClass
    public static void runOnceBeforeClass() {
        System.out.println("@BeforeClass - runOnceBeforeClass");
    }

    @AfterClass
    public static void runOnceAfterClass() {
        System.out.println("@AfterClass - runOnceAfterClass");
    }


    @Test
    public void testInput() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());

        presenter.titleChanged("new title");
        assertTrue(presenter.getTitle() == "new title");

        presenter.imageURIChanged("http://java.sun.com/j2se/1.3/");
        assertFalse(presenter.getImageUri() == null);
        assertTrue(presenter.getImageUri().equals("http://java.sun.com/j2se/1.3/"));

        presenter.imageURIChanged("hej");
        assertFalse(presenter.getImageUri() == "hej");

        presenter.descriptionChanged("new description");
        assertTrue(presenter.getDescription().equals("new description"));


    }
}
