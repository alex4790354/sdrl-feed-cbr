package com.github.alex4790354.general.configuration.quartzconfig;


import com.github.alex4790354.general.constant.DataNameConstant;
import com.github.alex4790354.general.constant.IdentityConstant;
import com.github.alex4790354.service.quartzJob.MetalRateLoadJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuartzMetalRateConfig {

    private final SchedulerProperties schedulerProperties;

    @Bean
    public JobDetail createJobDetailMatalRate() {
        return JobBuilder.newJob(MetalRateLoadJob.class)
                .withIdentity(IdentityConstant.JOB_METAL_RATES_IDENTITY.getIdentity(),
                        schedulerProperties.getSchedulers().get(DataNameConstant.METAL_RATE.getProperty()).permanentJobsGroupName)
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "schedulers.metal.enable", havingValue = "true")
    public Trigger showTimeTriggerMetalRate() {
        return TriggerBuilder.newTrigger()
                .forJob(createJobDetailMatalRate())
                .withIdentity(IdentityConstant.JOB_METAL_RATES_IDENTITY.getIdentity(),
                        schedulerProperties.getSchedulers().get(DataNameConstant.METAL_RATE.getProperty()).permanentJobsGroupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(schedulerProperties.getSchedulers().get(DataNameConstant.METAL_RATE.getProperty()).showTimeJobCron))
                .build();
    }

}
