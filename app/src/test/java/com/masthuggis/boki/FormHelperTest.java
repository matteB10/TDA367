package com.masthuggis.boki;

import com.masthuggis.boki.utils.FormHelper;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FormHelperTest {

    @BeforeClass
    public static void runOnceBeforeClass() {
        System.out.println("@BeforeClass - runOnceBeforeClass");
    }

    @AfterClass
    public static void runOnceAfterClass() {
        System.out.println("@AfterClass - runOnceAfterClass");
    }

    @Test
    public void testIsValidNumber() {
        FormHelper fm = FormHelper.getInstance();

        assertTrue(fm.isValidNumber("123"));
        assertFalse(fm.isValidNumber("hej"));
    }

    @Test
    public void testIsValidEmail() {
        FormHelper fm = FormHelper.getInstance();

        assertTrue(fm.isValidEmail("matblomq@chalmers.se"));
        assertTrue(fm.isValidEmail("matblomq@student.chalmers.se"));
        assertFalse(fm.isValidEmail("m@."));
        assertFalse(fm.isValidEmail("m@m."));
        assertFalse(fm.isValidEmail("matilda@se"));
    }

    @Test
    public void testIsValidMobile() {
        FormHelper fm = FormHelper.getInstance();

        assertTrue(fm.isValidMobile("0722318924"));
        assertTrue(fm.isValidMobile("072-2318924"));
        assertFalse(fm.isValidMobile("hej"));
        assertFalse(fm.isValidMobile("07-0838"));
        assertFalse(fm.isValidMobile("0-7777777777"));
    }

    @Test
    public void testIsValidPrice() {
        FormHelper fm = FormHelper.getInstance();

        assertFalse(fm.isValidPrice("555555"));
        assertFalse(fm.isValidPrice("Ogiltigt pris"));
        assertTrue(fm.isValidPrice("350"));
    }

}
