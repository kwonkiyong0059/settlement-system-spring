package com.sparta.projcalc.batch.statistics;

import com.sparta.projcalc.domain.video.entity.Video;
import lombok.RequiredArgsConstructor;
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
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch 설정을 위한 구성 클래스입니다.
 * 비디오 통계 작업과 스텝을 정의합니다.
 */
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class VideoStatisticsBatchConfig {

    private final JobRepository jobRepository;
    private final Step videoStatisticsStep;
    private final PlatformTransactionManager transactionManager;

    /**
     * 비디오 통계 작업을 정의하는 Job을 생성합니다.
     *
     * @return 설정된 Job.
     */
    @Bean("videoStatisticsJob")
    public Job videoStatisticsJob() {
        return new JobBuilder("videoStatisticsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(videoStatisticsStep)
                .end()
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
    @Bean("videoStatisticsStep")
    public Step videoStatisticsStep(ItemReader<Video> videoStatisticsItemReader,
                                    ItemProcessor<Video, VideoStatistics> videoStatisticsItemProcessor,
                                    ItemWriter<VideoStatistics> videoStatisticsItemWriter) {
        return new StepBuilder("videoStatisticsStep", jobRepository)
                .<Video, VideoStatistics>chunk(10)
                .reader(videoStatisticsItemReader)
                .processor(videoStatisticsItemProcessor)
                .writer(videoStatisticsItemWriter)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public ItemProcessor<Video, VideoStatistics> videoStatisticsItemProcessor() {
        return new VideoStatisticsItemProcessor();
    }
}
