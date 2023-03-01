package com.advertisements.statistics.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertiserRepository extends JpaRepository<Advertiser, Integer> {

    @Query("SELECT DISTINCT dataSource FROM Advertiser")
    List<String> findUniqueAdvertisers();

    @Query("SELECT SUM(a.clicks) FROM Advertiser a WHERE a.dataSource = ?3 and  a.daily BETWEEN ?1 AND ?2")
    Optional<Integer> findClicksAmountForDataSourceDateRange(Date dateFrom, Date dateTo, String dataSource);

    @Query("SELECT AVG(a.clicks) FROM Advertiser a WHERE a.dataSource = ?1 and a.campaign = ?2")
    Optional<Double> findAverageClicksForDataSourceAndCampaign(String dataSource, String campaign);

    @Query("SELECT SUM(a.impressions) FROM Advertiser a WHERE a.daily = ?1")
    Optional<Integer> findAdvertisersImpressionsFroDate(Date date);
}
