package com.advertisements.statistics.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "advertisers_statistics")
public class Advertiser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "data_source")
    private String dataSource;
    @Column(name = "campaign")
    private String campaign;
    @Column(name = "daily")
    private Date daily;
    @Column(name = "clicks")
    private int clicks;
    @Column(name = "impressions")
    private int impressions;

    public Advertiser() {
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Date getDaily() {
        return daily;
    }

    public void setDaily(Date daily) {
        this.daily = daily;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }
}
