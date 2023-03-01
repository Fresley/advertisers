package com.advertisements.statistics.utils;

import com.advertisements.statistics.entity.Advertiser;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvToAdvertiserTransformer {

    public static List<Advertiser> getAdvertisers() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("advertisersData.csv").toURI());
        return readLineByLine(path);
    }

    private static List<Advertiser> readLineByLine(Path filePath) throws Exception {
        List<Advertiser> list = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(filePath)) {
            try (CSVReader csvReader = getCustomCsvReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    list.add(createAdvertiser(line));
                }
            }
        }
        return list;
    }
    private static Advertiser createAdvertiser(String[] data) {
        Advertiser advertiser = new Advertiser();
        advertiser.setDataSource(data[0]);
        advertiser.setCampaign(data[1]);
        try {
            advertiser.setDaily(DateConverter.transformToSqlDateFromString(data[2]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        advertiser.setClicks(Integer.valueOf(data[3]));
        advertiser.setImpressions(Integer.valueOf(data[4]));

        return advertiser;
    }

    private static CSVReader getCustomCsvReader(Reader reader) {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        return new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();
    }

}
