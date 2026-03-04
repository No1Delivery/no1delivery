package com.sparta.no1delivery.global.infrastructure.event;

import com.sparta.no1delivery.domain.payment.domain.event.PaymentApprovedEvent;
import org.springframework.context.ApplicationEventPublisher;

public class Events {
    // 필드 만들어 두기
    private static ApplicationEventPublisher publisher;

    static void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    public static void trigger(Object event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
    }
}
