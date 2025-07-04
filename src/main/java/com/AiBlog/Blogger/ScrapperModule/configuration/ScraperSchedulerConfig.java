package com.AiBlog.Blogger.ScrapperModule.configuration;

import com.AiBlog.Blogger.ScrapperModule.Service.RssFetcherService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class ScraperSchedulerConfig {
    private final RssFetcherService rssFetcherService;

    public ScraperSchedulerConfig(RssFetcherService rssFetcherService) {
        this.rssFetcherService = rssFetcherService;
    }


//    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Kolkata")
    @PostConstruct
    public void scheduleRssFetch() {
        System.out.println("Starting RSS fetch at: " + LocalDateTime.now());
        rssFetcherService.fetchFeedsAndQueueTasks();
        System.out.println("RSS fetch completed at: " + LocalDateTime.now());
    }
}
