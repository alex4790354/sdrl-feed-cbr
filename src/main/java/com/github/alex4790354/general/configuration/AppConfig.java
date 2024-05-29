package com.github.alex4790354.general.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties
public class AppConfig {

    @Value("${sdrl-feed-cbr.var.hist-cur-rates-start-date}")
    private String histCurRatesStartDate;

    @Value("${sdrl-feed-cbr.var.hist-metal-rates-start-date}")
    private String histMetalRatesStartDate;

    @Value("${sdrl-feed-cbr.var.metal-start-day-from-today}")
    private int metalStartDayFromToday;

    // SchedulerConfig:
    @Value("${sdrl-feed-cbr.var.currency-time-job-cron}")
    private String currencyTimeJobCron;

    @Value("${sdrl-feed-cbr.var.rates-time-job-cron}")
    private String ratesTimeJobCron;

    @Value("${sdrl-feed-cbr.var.metal-time-job-cron}")
    private String metalTimeJobCron;

}

