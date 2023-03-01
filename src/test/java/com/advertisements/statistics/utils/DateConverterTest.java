package com.advertisements.statistics.utils;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateConverterTest {

    @Test
    void whenGivenStringDate_thenReturnSqlDate() throws ParseException {
        String inputDate = "01/31/2022";
        java.sql.Date expectedDate = convertStringToDate(inputDate);

        assertThat(DateConverter.transformToSqlDateFromString(inputDate)).isEqualTo(expectedDate);
    }

    @Test
    void whenGivenTwoDatesSecondIsBeforeFirst_thenThrowException() throws ParseException {
        String firstDate = "01/31/2021";
        java.sql.Date sqlFirstDate = convertStringToDate(firstDate);
        String secondDate = "01/31/2022";
        java.sql.Date sqlSecondDate = convertStringToDate(secondDate);

        assertThatThrownBy(() -> {
            DateConverter.compareIfSecondDateIsAfter(sqlSecondDate, sqlFirstDate);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenGivenTwoDatesSecondIsAfterFirst_thenDoNotThrowException() throws ParseException {
        String firstDate = "01/31/2021";
        java.sql.Date sqlFirstDate = convertStringToDate(firstDate);
        String secondDate = "01/31/2022";
        java.sql.Date sqlSecondDate = convertStringToDate(secondDate);
        DateConverter.compareIfSecondDateIsAfter(sqlFirstDate, sqlSecondDate);
    }

    private java.sql.Date convertStringToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        java.util.Date javaDate = dateFormat.parse(date);
        return new java.sql.Date(javaDate.getTime());
    }
}
