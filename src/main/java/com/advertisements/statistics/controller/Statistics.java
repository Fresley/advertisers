package com.advertisements.statistics.controller;

import com.advertisements.statistics.entity.Advertiser;
import com.advertisements.statistics.model.DataSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/statistics")
public class Statistics {

    @Autowired
    private DataSupplier service;

    @GetMapping("/advertisers")
    public ResponseEntity<List<String>> getUniqueAdvertisers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAllAdvertisers());
    }

    @GetMapping("/clicks/{dataSource}/{dateFrom}/{dateTo}")
    public ResponseEntity<Integer> getClicksAmountForDataSourceDateRange(
            @PathVariable("dataSource") String dataSource,
            @PathVariable("dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date dateFrom,
            @PathVariable("dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date dateTo) {

        Optional<Integer> result;

        try {
            result = service.getClicksForDataSourceDateRange(dataSource, dateFrom, dateTo);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Provided dimensions are not correct"
            );
        }
        if (result.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT,
                    "No results found");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.get());
    }

    @GetMapping("/clicks/average/{dataSource}/{campaign}")
    public ResponseEntity<Double> getAverageClicksForDataSourceAndCampaign(
            @PathVariable("dataSource") String dataSource,
            @PathVariable("campaign") String campaign) {

        Optional<Double> result = service.getAverageClicksAmountForDataSourceAndCampaign(dataSource, campaign);

        if (result.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT,
                    "No results found");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.get());
    }

    @GetMapping("/impressions/daily/{date}")
    public ResponseEntity<Integer> getImpressionsForDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date date) {
        Optional<Integer> result = service.getImpressionsForDate(date);

        if (result.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT,
                    "No results found");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.get());
    }
}
