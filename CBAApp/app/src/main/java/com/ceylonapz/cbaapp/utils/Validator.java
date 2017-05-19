package com.ceylonapz.cbaapp.utils;

/**
 * Created by amalskr on 2017-05-19.
 */
public class Validator {

    //CHECK ONLY STRING
    public static boolean isAlpha(String name) {
        if (name != null && name.length() > 0) {
            return name.matches("^[a-zA-Z_ ]*$");
        } else {
            return false;
        }

    }

    //CHECK ONLY NUMBER
    public static boolean isAmount(String number) {

        if (number != null && number.length() > 0) {
            try {
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isNumber(String number) {
        if (number != null && number.length() > 0) {
            return number.matches("^[0-9_ ]*$");
        } else {
            return false;
        }

    }

    //CHECKING GOOD EMAIL
    public static boolean isValidEmail(CharSequence email) {
        if (email == null) {
            return false;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return isGoodEmail(email.toString());
            } else {
                return false;
            }

        }
    }

    //CHECK EMAIL
    public static boolean isGoodEmail(String email) {

        String[] emlAry = email.replace(".", "@").split("@");
        String NAME = emlAry[0]; //AMAL
        String NS = emlAry[1].trim(); //GMAIL
        String DOMAIN = emlAry[2].trim(); //COM

        if (NAME.length() >= 3) {
            if (NS.length() >= 3) {
                if (DOMAIN.length() >= 2) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
