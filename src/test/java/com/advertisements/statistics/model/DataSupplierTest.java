package com.advertisements.statistics.model;

import com.advertisements.statistics.entity.AdvertiserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSupplierTest {

    @Mock
    private AdvertiserRepository repository;
    @InjectMocks
    private DataSupplier dataSupplier;

    @Test
    void whenGetAllAdvertisersCalled_thenReturnValues() {
        List<String> expectedResult = new ArrayList<>(Arrays.asList("Data Source One", "Data Source Two"));

        when(repository.findUniqueAdvertisers()).thenReturn(expectedResult);

        assertThat(dataSupplier.getAllAdvertisers()).isEqualTo(expectedResult);
    }

    @Test
    void whenGettingClicksForDataSourceWithDateRange_thenReturnAmount() throws ParseException {
        Integer expectedResult = 5;
        String dataSource = "dataSource";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = dateFormat.parse("2020-12-31");
        Date dateTo = dateFormat.parse("2022-12-31");
        java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

        when(repository.findClicksAmountForDataSourceDateRange(sqlDateFrom, sqlDateTo, dataSource)).thenReturn(Optional.of(expectedResult));

        Optional<Integer> result = dataSupplier.getClicksForDataSourceDateRange(dataSource, dateFrom, dateTo);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(expectedResult);
    }

    @Test
    void whenGettingClicksForDataSourceWithDateRange_thenReturnEmpty() throws ParseException {
        String dataSource = "Not a dataSource";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = dateFormat.parse("2022-12-31");
        Date dateTo = dateFormat.parse("2023-12-31");
        java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

        when(repository.findClicksAmountForDataSourceDateRange(sqlDateFrom, sqlDateTo, dataSource)).thenReturn(Optional.empty());

        Optional<Integer> result = dataSupplier.getClicksForDataSourceDateRange(dataSource, dateFrom, dateTo);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void whenGettingClicksForDataSourceWithInvalidDateRange_thenReturnException() throws ParseException {
        String dataSource = "dataSource";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = dateFormat.parse("2022-12-31");
        Date dateTo = dateFormat.parse("2020-12-31");

        assertThatThrownBy(() -> {
            dataSupplier.getClicksForDataSourceDateRange(dataSource, dateFrom, dateTo);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenGetAverageClicksAmountForDataSourceAndCampaign_thenReturnValue() {
        Double expectedResult = 30.0;
        String dataSource = "Not Data Source One";
        String campaign = "Not Campaign One";

        when(repository.findAverageClicksForDataSourceAndCampaign(dataSource, campaign)).thenReturn(Optional.of(expectedResult));

        Optional<Double> result = dataSupplier.getAverageClicksAmountForDataSourceAndCampaign(dataSource, campaign);

        assertThat(result.get()).isEqualTo(expectedResult);
    }

    @Test
    void whenGettingImpressionsAmountForDateWithoutResult_thenReturnStatusCodeNoContent() throws ParseException {
        Integer expectedResult = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2022-12-31");
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        when(repository.findAdvertisersImpressionsFroDate(sqlDate)).thenReturn(Optional.of(expectedResult));

        Optional<Integer> result = dataSupplier.getImpressionsForDate(date);

        assertThat(result.get()).isEqualTo(expectedResult);
    }
}
