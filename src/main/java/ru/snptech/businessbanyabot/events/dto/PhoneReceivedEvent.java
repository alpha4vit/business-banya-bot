package ru.snptech.businessbanyabot.events.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PhoneReceivedEvent extends ApplicationEvent {
    private final String dealId;

    public PhoneReceivedEvent(Object source, String dealId) {
        super(source);
        this.dealId = dealId;
    }
}
