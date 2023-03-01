package com.advertisements.statistics.model;

import com.advertisements.statistics.entity.Advertiser;
import com.advertisements.statistics.entity.AdvertiserRepository;
import com.advertisements.statistics.utils.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DataSupplier {

    @Autowired
    private AdvertiserRepository repository;

    public List<String> getAllAdvertisers() {
        return repository.findUniqueAdvertisers();
    }

    public Optional<Integer> getClicksForDataSourceDateRange(String dataSource, Date dateFrom, Date dateTo) {
        java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

        DateConverter.compareIfSecondDateIsAfter(sqlDateFrom, sqlDateTo);

        return repository.findClicksAmountForDataSourceDateRange(sqlDateFrom, sqlDateTo, dataSource);
    }

    public Optional<Double> getAverageClicksAmountForDataSourceAndCampaign(String dataSource, String campaign) {
        return repository.findAverageClicksForDataSourceAndCampaign(dataSource, campaign);
    }

    public Optional<Integer> getImpressionsForDate(Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        return repository.findAdvertisersImpressionsFroDate(sqlDate);
    }
}
