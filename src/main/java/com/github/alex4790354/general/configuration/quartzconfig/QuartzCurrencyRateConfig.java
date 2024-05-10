package com.github.alex4790354.general.configuration.quartzconfig;


import com.github.alex4790354.general.constant.DataNameConstant;
import com.github.alex4790354.general.constant.IdentityConstant;
import com.github.alex4790354.service.quartzJob.CurrencyRateLoadJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuartzCurrencyRateConfig {

    private final SchedulerProperties schedulerProperties;

    @Bean
    public JobDetail createJobDetailOverTheCounter() {
        return JobBuilder.newJob(CurrencyRateLoadJob.class)
                .withIdentity(IdentityConstant.JOB_CURRENCY_RATES_IDENTITY.getIdentity(),
                        schedulerProperties.getSchedulers().get(DataNameConstant.CURRENCY_RATE.getProperty()).permanentJobsGroupName)
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "schedulers.rates.enable", havingValue = "true")
    public Trigger showTimeTriggerCurrencyRates() {
        return TriggerBuilder.newTrigger()
                .forJob(createJobDetailOverTheCounter())
                .withIdentity(IdentityConstant.JOB_CURRENCY_RATES_IDENTITY.getIdentity(),
                        schedulerProperties.getSchedulers().get(DataNameConstant.CURRENCY_RATE.getProperty()).permanentJobsGroupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(schedulerProperties.getSchedulers().get(DataNameConstant.CURRENCY_RATE.getProperty()).showTimeJobCron))
                .build();
    }

}
