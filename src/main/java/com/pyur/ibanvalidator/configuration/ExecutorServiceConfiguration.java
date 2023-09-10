package com.pyur.ibanvalidator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass=true)
public class ExecutorServiceConfiguration {

  @Bean
  public ExecutorService executorService() {
    return Executors.newFixedThreadPool(25);
  }

  @Bean("scheduledExecutorService")
  public ExecutorService scheduledExecutorService() {
    return Executors.newScheduledThreadPool(25);
  }

  @Bean("taskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("poolThread-");
    executor.initialize();
    return executor;
  }

}
