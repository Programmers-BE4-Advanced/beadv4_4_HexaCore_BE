package com.back.settlement.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 월별 정산 배치 Job 설정
*/
@Configuration
@RequiredArgsConstructor
public class SettlementJobConfig {
    @Bean
    public Job settlementJob(JobRepository jobRepository, Step fetchOrdersAndCreateItemsStep, Step collectItemsAndCalculateSettlementsStep, Step monthSettlementStep) {
        return new JobBuilder("settlementJob", jobRepository)
                .start(fetchOrdersAndCreateItemsStep)
                .next(collectItemsAndCalculateSettlementsStep)
                .next(monthSettlementStep)
                .build();
    }
}
