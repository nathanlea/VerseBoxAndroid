package com.fibonaccistudios.nathanlea.versebox;

import java.util.Calendar;

public class VerseBoxCal {
    /*
    http://stackoverflow.com/questions/1021324/java-code-for-calculating-leap-year
    */
    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }
}