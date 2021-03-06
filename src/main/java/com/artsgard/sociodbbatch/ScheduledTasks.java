package com.artsgard.sociodbbatch;

import com.artsgard.sociodbbatch.socio.repository.AssociatedSocioRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 * @author artsgard
 */

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("sociojob")
    private Job job;

    public static final int ONE_SINGLE_DAY = 86400000;
    
    @Autowired
    private AssociatedSocioRepository repo;

    // second, minute, hour, day of month, month, day(s) of week
    @Scheduled(cron = "0 10 10 * * *", zone = "Europe/Paris")

    public void schedule() throws JobExecutionAlreadyRunningException, JobRestartException, JobParametersInvalidException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("sociojob-date", new Date())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);
        log.info("execution.getStatus(): " + execution.getStatus());
        log.info("Time of execution: ", dateFormat.format(new Date())); 
    }
}
