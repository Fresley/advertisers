package com.advertisements.statistics.utils;

import com.advertisements.statistics.entity.AdvertiserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInserter {

    @Autowired
    private AdvertiserRepository advertiserRepository;

    @PostConstruct
    public void insertDataToDatabase() {
        try {
            advertiserRepository.saveAll(CsvToAdvertiserTransformer.getAdvertisers());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
