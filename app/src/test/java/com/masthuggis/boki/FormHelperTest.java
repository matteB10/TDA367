package com.masthuggis.boki;

import com.masthuggis.boki.presenter.FormHelper;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class FormHelperTest {

    @Test
    public void testValidations(){
        FormHelper fm = FormHelper.getInstance();

        assertTrue(fm.isValidNumber("123"));
        assertFalse(fm.isValidNumber("hej"));

        assertTrue(fm.isValidEmail("matblomq@chalmers.se"));
        assertTrue(fm.isValidEmail("matblomq@student.chalmers.se"));
        assertFalse(fm.isValidEmail("m@."));
        assertFalse(fm.isValidEmail("m@m."));
        assertFalse(fm.isValidEmail("matilda@se"));

        assertTrue(fm.isValidMobile("0722318924"));
        assertTrue(fm.isValidMobile("072-2318924"));
        assertFalse(fm.isValidMobile("hej"));

        //TODO: More tests
    }
}
