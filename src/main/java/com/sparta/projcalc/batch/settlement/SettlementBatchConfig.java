package com.sparta.projcalc.batch.settlement;

import com.sparta.projcalc.domain.video.entity.Video;
import com.sparta.projcalc.domain.video.repository.VideoRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class SettlementBatchConfig {

    private final VideoRepository videoRepository;
    private final SettlementRepository settlementRepository;
    private final EntityManagerFactory entityManagerFactory;

    public SettlementBatchConfig(VideoRepository videoRepository, SettlementRepository settlementRepository, EntityManagerFactory entityManagerFactory) {
        this.videoRepository = videoRepository;
        this.settlementRepository = settlementRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job settlementJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("settlementJob", jobRepository)
                .start(settlementStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step settlementStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("settlementStep", jobRepository)
                .<Video, Settlement>chunk(10) // 데이터 청크 크기
                .reader(videoItemReader()) // ItemReader 설정
                .processor(settlementProcessor()) // ItemProcessor 설정
                .writer(settlementWriter()) // ItemWriter 설정
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Video> videoItemReader() {
        return new JpaPagingItemReaderBuilder<Video>()
                .name("videoItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT v FROM Video v")
                .pageSize(10)
                .build();
    }

    @Bean
    public ItemProcessor<Video, Settlement> settlementProcessor() {
        return new VideoToSettlementProcessor();
    }

    @Bean
    public ItemWriter<Settlement> settlementWriter() {
        return new SettlementItemWriter(settlementRepository);
    }
}
