package com.attendance_maangement_system.attendance_management_system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class ThreadPoolConfig {
    @Bean(name = "multithreadingbean")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Number of threads to keep alive
        executor.setMaxPoolSize(20); // Maximum number of threads in the pool
        executor.setQueueCapacity(50); // Queue capacity for pending tasks
        executor.setThreadNamePrefix("AttendanceManagementThread-");
        executor.initialize();
        return executor;
    }
}
