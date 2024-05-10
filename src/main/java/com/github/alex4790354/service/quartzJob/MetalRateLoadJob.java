package com.github.alex4790354.service.quartzJob;


import com.github.alex4790354.service.FeedCbrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class MetalRateLoadJob extends QuartzJobBean {

    private final FeedCbrService service;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        service.getMetalRatesDaily();

    }
}
