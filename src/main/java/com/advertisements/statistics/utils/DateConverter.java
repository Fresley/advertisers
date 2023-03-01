package com.advertisements.statistics.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateConverter {

    public static java.sql.Date transformToSqlDateFromString(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        java.util.Date javaDate = dateFormat.parse(date);
        return new java.sql.Date(javaDate.getTime());
    }

    public static void compareIfSecondDateIsAfter(java.sql.Date dateFrom, java.sql.Date dateTo) {
        if (dateFrom.compareTo(dateTo) > 0) throw new IllegalArgumentException();
    }
}
