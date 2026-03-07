package com.sparta.no1delivery.global.infrastructure.event;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class EventConfig implements AsyncConfigurer {

    private ApplicationEventPublisher publisher;

    @Bean
    public InitializingBean eventsInitializer() {
        return () -> Events.setPublisher(publisher);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 기본 스레드 수
        executor.setMaxPoolSize(10);// 최대 스레드 수
        executor.setQueueCapacity(50); //대기 큐의 용량
        executor.setThreadNamePrefix("Async-"); // 스레드 이름 접두사?
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
