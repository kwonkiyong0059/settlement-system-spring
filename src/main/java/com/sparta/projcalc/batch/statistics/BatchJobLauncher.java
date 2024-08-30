package com.sparta.projcalc.batch.statistics;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

/**
 * 배치 작업을 실행하는 컴포넌트입니다.
 */
@Component
public class BatchJobLauncher {

    private final JobLauncher jobLauncher;
    private final Job videoStatisticsJob;

    /**
     * BatchJobLauncher의 생성자입니다.
     *
     * @param jobLauncher 배치 작업을 실행하는 JobLauncher.
     * @param videoStatisticsJob 비디오 통계 작업을 정의한 Job.
     */
    public BatchJobLauncher(JobLauncher jobLauncher, Job videoStatisticsJob) {
        this.jobLauncher = jobLauncher;
        this.videoStatisticsJob = videoStatisticsJob;
    }

    /**
     * 비디오 통계 작업을 실행합니다.
     *
     * @param periodType 통계 기간을 매개변수로 전달.
     */
    public void runVideoStatisticsJob(String periodType) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("periodType", periodType)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(videoStatisticsJob, jobParameters);
            // 작업 실행 상태 로그 또는 확인
        } catch (Exception e) {
            // 예외 처리 및 로그
            e.printStackTrace();
        }
    }
}
