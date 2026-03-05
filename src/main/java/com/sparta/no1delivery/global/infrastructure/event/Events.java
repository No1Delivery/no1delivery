package com.sparta.no1delivery.global.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;

public class Events {
    // 필드 만들어 두기
    private static ApplicationEventPublisher publisher;

    static void setPublisher(ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    public static void trigger(Object event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
    }
}
