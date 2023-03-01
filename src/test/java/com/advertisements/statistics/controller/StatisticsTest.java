package com.advertisements.statistics.controller;

import com.advertisements.statistics.model.DataSupplier;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatisticsTest {

    @LocalServerPort
    private int port;
    private String uri;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    @MockBean
    private DataSupplier service;

    @Test
    void whenGetAllAdvertisersCalled_thenReturnValues() {
        List<String> testResult = new ArrayList<>(Arrays.asList("Data Source One", "Data Source Two"));

        when(service.getAllAdvertisers()).thenReturn(testResult);

        Object[] result = get(uri + "/statistics/advertisers").then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Object[].class);

        assertThat(Arrays.asList(result)).isEqualTo(testResult);
    }

    @Test
    void whenGettingClicksForValidDataSourceWithDateRange_thenReturnAmount() throws ParseException {
        String dateFrom = "2020-12-31";
        String dateTo = "2022-12-31";
        String dataSource = "Data Source One";
        int resultClicks = 5;

        when(service.getClicksForDataSourceDateRange(anyString(), any(), any())).thenReturn(Optional.of(resultClicks));

        int result = get(uri + "/statistics/clicks/{dataSource}/{dateFrom}/{dateTo}", dataSource, dateFrom, dateTo).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Integer.class);

        assertThat(result).isEqualTo(resultClicks);
    }

    @Test
    void whenGettingClicksForValidDataSourceWithNotConsistentDateRange_thenReturnStatusBadRequest() throws ParseException {
        String dateFrom = "2019-12-31";
        String dateTo = "2018-12-31";
        String dataSource = "Data Source One ";

        when(service.getClicksForDataSourceDateRange(anyString(), any(), any())).thenThrow(IllegalArgumentException.class);

        get(uri + "/statistics/clicks/{dataSource}/{dateFrom}/{dateTo}", dataSource, dateFrom, dateTo).then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenGettingClicksForValidDataWithoutResult_thenReturnStatusNoContent() throws ParseException {
        String dateFrom = "2011-12-31";
        String dateTo = "2012-12-31";
        String dataSource = "Data Source One ";

        when(service.getClicksForDataSourceDateRange(anyString(), any(), any())).thenReturn(Optional.empty());

        get(uri + "/statistics/clicks/{dataSource}/{dateFrom}/{dateTo}", dataSource, dateFrom, dateTo).then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void whenGettingAverageClicksForValidDataSourceAndValidCampaign_thenReturnAmount() {
        Double expectedResult = 30.0;
        String dataSource = "Data Source One";
        String campaign = "Campaign One";

        when(service.getAverageClicksAmountForDataSourceAndCampaign(anyString(), anyString())).thenReturn(Optional.of(expectedResult));

        Double result = get(uri + "/statistics/clicks/average/{dataSource}/{campaign}", dataSource, campaign).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Double.class);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void whenGettingAverageClicksForValidDataSourceAndValidCampaignWithoutResult_thenReturnStatusNoContent() {
        String dataSource = "Not Data Source One";
        String campaign = "Not Campaign One";

        when(service.getAverageClicksAmountForDataSourceAndCampaign(anyString(), anyString())).thenReturn(Optional.empty());

        get(uri + "/statistics/clicks/average/{dataSource}/{campaign}", dataSource, campaign).then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void whenGettingImpressionsAmountForDate_thenReturnAmount() {
        String date = "2021-12-31";
        Integer expectedResult = 100;

        when(service.getImpressionsForDate(any())).thenReturn(Optional.of(expectedResult));

        int result = get(uri + "/statistics/impressions/daily/{date}", date).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Integer.class);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void whenGettingImpressionsAmountForDateWithoutResult_thenReturnStatusCodeNoContent() {
        String date = "1666-12-31";

        when(service.getImpressionsForDate(any())).thenReturn(Optional.empty());

        get(uri + "/statistics/impressions/daily/{date}", date).then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
