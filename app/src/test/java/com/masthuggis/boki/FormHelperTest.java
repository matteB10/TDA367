package com.masthuggis.boki;

import com.masthuggis.boki.utils.FormHelper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        assertTrue(FormHelper.isValidNumber("123"));
        assertFalse(FormHelper.isValidNumber("hej123"));
        assertFalse(FormHelper.isValidNumber("hej"));
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(FormHelper.isValidEmail("matblomq@chalmers.se"));
        assertTrue(FormHelper.isValidEmail("matblomq@student.chalmers.se"));
        assertFalse(FormHelper.isValidEmail("m@."));
        assertFalse(FormHelper.isValidEmail("m@m."));
        assertFalse(FormHelper.isValidEmail("kalle@se"));
    }

    @Test
    public void testIsValidMobile() {
        assertTrue(FormHelper.isValidMobile("0722318924"));
        assertTrue(FormHelper.isValidMobile("072-2318924"));
        assertFalse(FormHelper.isValidMobile("hej"));
        assertFalse(FormHelper.isValidMobile("mm121212mm"));
        assertFalse(FormHelper.isValidMobile("07-0838"));
        assertFalse(FormHelper.isValidMobile("0-7777777777"));
    }

    @Test
    public void testIsValidPrice() {

        assertFalse(FormHelper.isValidPrice("555555"));
        assertFalse(FormHelper.isValidPrice("Ogiltigt pris"));

        assertTrue(FormHelper.isValidPrice("350"));
        assertTrue(FormHelper.isValidPrice("0"));
    }


}
