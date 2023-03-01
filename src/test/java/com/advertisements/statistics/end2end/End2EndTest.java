package com.advertisements.statistics.end2end;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class End2EndTest {

    @LocalServerPort
    private int port;
    private String uri;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    @Test
    void whenGettingAdvertisers_thenReturnList() {
        Object[] result = get(uri + "/statistics/advertisers").then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Object[].class);

        assertThat(Arrays.asList(result)).isNotEmpty();
    }

    @Test
    void whenGettingClicksForValidDataSourceWithDateRange_thenReturnAmount() {
        String dateFrom = "2020-12-31";
        String dateTo = "2022-12-31";
        String dataSource = "Data Source One";

        int result = get(uri + "/statistics/clicks/{dataSource}/{dateFrom}/{dateTo}", dataSource, dateFrom, dateTo).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Integer.class);

        assertThat(result).isEqualTo(5);
    }

    @Test
    void whenGettingClicksForValidDataSourceWithNotConsistentDateRange_thenReturnStatusBadRequest() {
        String dateFrom = "2019-12-31";
        String dateTo = "2018-12-31";
        String dataSource = "Data Source One ";

        get(uri + "/statistics/clicks/{dataSource}/{dateFrom}/{dateTo}", dataSource, dateFrom, dateTo).then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenGettingClicksForValidDataSourceWithInvalidDateRange_thenReturnStatusNoContent() {
        String dateFrom = "2018-12-31";
        String dateTo = "2019-12-31";
        String dataSource = "Data Source One";

        get(uri + "/statistics/clicks/{dataSource}/{dateFrom}/{dateTo}", dataSource, dateFrom, dateTo).then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void whenGettingAverageClicksForValidDataSourceAndValidCampaign_thenReturnAmount() {
        Double expectedResult = 30.0;
        String dataSource = "Data Source One";
        String campaign = "Campaign One";

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

        get(uri + "/statistics/clicks/average/{dataSource}/{campaign}", dataSource, campaign).then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void whenGettingImpressionsAmountForDate_thenReturnAmount() {
        String date = "2021-12-31";

        int result = get(uri + "/statistics/impressions/daily/{date}", date).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Integer.class);

        assertThat(result).isEqualTo(100);
    }

    @Test
    void whenGettingImpressionsAmountForDateWithoutResult_thenReturnStatusCodeNoContent() {
        String date = "1666-12-31";

        get(uri + "/statistics/impressions/daily/{date}", date).then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
