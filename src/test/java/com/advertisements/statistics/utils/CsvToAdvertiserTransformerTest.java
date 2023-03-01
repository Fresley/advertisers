package com.advertisements.statistics.utils;

import com.advertisements.statistics.entity.Advertiser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvToAdvertiserTransformerTest {

    @Test
    void whenGetAdvertisersCalled_thenReturnList() throws Exception {
        List<Advertiser> result = CsvToAdvertiserTransformer.getAdvertisers();

        assertThat(result).isNotEmpty();
    }
}
