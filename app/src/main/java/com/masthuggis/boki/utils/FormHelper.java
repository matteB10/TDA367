package com.masthuggis.boki.utils;

import java.util.regex.Pattern;

/**
 * Helper class for form logic.
 * Used by CreateAdPresenter and SearchHelper.
 * Written by masthuggis.
 */

public class FormHelper{

    private static FormHelper formHelper;


    private static final Pattern validDigits = Pattern.compile("[0-9]+");
    private static final Pattern validPrice = Pattern.compile("^[0-9]{1,4}$");
    private static final Pattern validEmail =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern validMobilePhone = Pattern.compile("^[0-9]{10}$");
    private static final Pattern validMobilePhoneWithFormat = Pattern.compile("^[0-9]{3}+-[0-9]{7}$");


    private FormHelper(){}


    public static FormHelper getInstance(){
        if(formHelper == null){
            formHelper = new FormHelper();
        }
        return formHelper;
    }


    /**
     * @param input the string to be validated
     * @return true if input string only contains digits
     */
     public static boolean isValidNumber(String input){
        return validDigits.matcher(input).matches();
    }
    /**
     * @param input the string to be validated
     * @return true if input string matching e-mail format (letters + @ + letters + . + letters)
     */
     public static boolean isValidEmail(String input){
        return validEmail.matcher(input).matches();
     }
    /**
     * @param input the string to be validated
     * @return true if input string matching mobile phone format (10 digits)
     */
     public static boolean isValidMobile(String input){

         return validMobilePhone.matcher(input).matches() || validMobilePhoneWithFormat.matcher(input).matches();
    }
    /**
     * @param input the string to be validated
     * @return true if input string matching mobile phone format (10 digits)
     */
    public static boolean isValidPrice(String input){
        return validPrice.matcher(input).matches();
    }

    public static int getValidMaxPrice(){
        return 9999;
    }


}
