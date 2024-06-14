package com.github.alex4790354.service.scheduler;

import com.github.alex4790354.general.configuration.AppConfig;
import com.github.alex4790354.general.constant.RequestFrequency;
import com.github.alex4790354.service.FeedCbrService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CronTasks {

    private final FeedCbrService service;
    private final AppConfig appConfig;

    @Scheduled(cron = "#{@appConfig.currencyTimeJobCron}")
    public void performTask() {
        service.getCurrenciesList(RequestFrequency.DAILY.getValue());
    }

    @Scheduled(cron = "#{@appConfig.ratesTimeJobCron}")
    public void currencyRateCronjob() {
        service.getCurrencyRates("");
    }

    @Scheduled(cron = "#{@appConfig.metalTimeJobCron}")
    public void metalRateCronjob() {
        service.getMetalRatesDaily();
    }

}
