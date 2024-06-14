package com.github.alex4790354.general.configuration.quartzconfig;

/**
 Quartz schedulers version. Uncomment if need it.
 */

/*import com.github.alex4790354.general.constant.DataNameConstant;
import com.github.alex4790354.general.constant.IdentityConstant;
import com.github.alex4790354.service.quartzJob.CurrenciesLoadJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor*/
public class QuartzCurrencyConfig {

    /*private final SchedulerProperties schedulerProperties;

    @Bean
    public JobDetail createJobDetailType() {
        return JobBuilder.newJob(CurrenciesLoadJob.class)
                .withIdentity(IdentityConstant.JOB_CURRENCY_IDENTITY.getIdentity(),
                        schedulerProperties.getSchedulers().get(DataNameConstant.CURRENCY.getProperty()).permanentJobsGroupName)
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "schedulers.currency.enable", havingValue = "true")
    public Trigger showTimeTriggerCurrencies() {
        return TriggerBuilder.newTrigger()
                .forJob(createJobDetailType())
                .withIdentity(IdentityConstant.JOB_CURRENCY_IDENTITY.getIdentity(),
                        schedulerProperties.getSchedulers().get(DataNameConstant.CURRENCY.getProperty()).permanentJobsGroupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(schedulerProperties.getSchedulers().get(DataNameConstant.CURRENCY.getProperty()).showTimeJobCron))
                .build();
    }*/
}
