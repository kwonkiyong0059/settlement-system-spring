package com.sparta.projcalc.batch.statistics;

import com.sparta.projcalc.domain.video.entity.Video;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Batch 설정을 위한 구성 클래스입니다.
 * 배치 작업과 스텝을 정의합니다.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;

    /**
     * BatchConfig의 생성자입니다.
     *
     * @param jobRepository Job을 생성하는 데 사용되는 JobRepository.
     */
    public BatchConfig(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * 비디오 통계 작업을 정의하는 Job을 생성합니다.
     *
     * @param videoStatisticsStep 비디오 통계를 처리할 Step.
     * @return 설정된 Job.
     */
    @Bean
    public Job videoStatisticsJob(Step videoStatisticsStep) {
        JobBuilder jobBuilder = new JobBuilder("videoStatisticsJob", jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer()) // Job 파라미터를 증가시켜 재실행 가능하게 함.
                .flow(videoStatisticsStep) // 실행할 Step을 정의.
                .end() // Job의 끝.
                .build();
    }

    /**
     * 비디오 통계를 읽고, 처리하고, 저장하는 Step을 정의합니다.
     *
     * @param videoStatisticsItemReader 비디오 데이터를 읽어오는 ItemReader.
     * @param videoStatisticsItemProcessor 비디오 데이터를 처리하는 ItemProcessor.
     * @param videoStatisticsItemWriter 비디오 통계를 저장하는 ItemWriter.
     * @return 설정된 Step.
     */
    @Bean
    public Step videoStatisticsStep(ItemReader<Video> videoStatisticsItemReader,
                                    ItemProcessor<Video, VideoStatistics> videoStatisticsItemProcessor,
                                    ItemWriter<VideoStatistics> videoStatisticsItemWriter) {
        StepBuilder stepBuilder = new StepBuilder("videoStatisticsStep", jobRepository);
        return stepBuilder
                .<Video, VideoStatistics>chunk(10) // 10개의 아이템 단위로 처리.
                .reader(videoStatisticsItemReader) // ItemReader 설정.
                .processor(videoStatisticsItemProcessor) // ItemProcessor 설정.
                .writer(videoStatisticsItemWriter) // ItemWriter 설정.
                .build();
    }
}
