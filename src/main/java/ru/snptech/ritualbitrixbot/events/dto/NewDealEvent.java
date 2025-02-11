package ru.snptech.ritualbitrixbot.events.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.snptech.ritualbitrixbot.model.ShopDealData;

@Getter
public class NewDealEvent extends ApplicationEvent {
    private final String dealId;
    private final String source;
    private final String source2;
    private final String dealType;
    private final String chatId;

    public NewDealEvent(Object source, String dealId, String source1, String source2, String dealType, String chatId) {
        super(source);
        this.dealId = dealId;
        this.source = source1;
        this.source2 = source2;
        this.dealType = dealType;
        this.chatId = chatId;
    }
}
