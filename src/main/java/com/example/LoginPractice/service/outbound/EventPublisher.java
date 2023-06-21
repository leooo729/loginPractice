package com.example.LoginPractice.service.outbound;

import com.example.LoginPractice.eventlistener.EventListener;
import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher{
    private final EventBus eventBus;

    public EventPublisher(EventListener infoEventListener) {
        this.eventBus = new EventBus();
        eventBus.register(infoEventListener);
    }

    public void publish(Object event) {
        eventBus.post(event);
    }
}
