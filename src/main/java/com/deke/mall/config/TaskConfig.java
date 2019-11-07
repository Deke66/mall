package com.deke.mall.config;

import com.deke.mall.task.ApplicationThreadPoolTaskExecutor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "custom.task.execution")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        return new ApplicationThreadPoolTaskExecutor();
    }
}
