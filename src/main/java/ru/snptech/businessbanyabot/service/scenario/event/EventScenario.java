package ru.snptech.businessbanyabot.service.scenario.event;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.Event;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixFileClient;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.step.EventScenarioStep;
import ru.snptech.businessbanyabot.model.scenario.step.SearchScenarioStep;
import ru.snptech.businessbanyabot.model.search.SearchMetadata;
import ru.snptech.businessbanyabot.model.search.SlideDirection;
import ru.snptech.businessbanyabot.repository.EventRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.util.TimeUtils;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class EventScenario extends AbstractScenario {

    private final UserContextService userContextService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BitrixFileClient bitrixFileClient;

    private static final Integer INITIAL_EVENT_SLIDER_INDEX = 0;

    Comparator<Event> eventComparator = Comparator
        .comparing(Event::getCarryDate, Comparator.nullsLast(Comparator.naturalOrder()))
        .thenComparing(Event::getWeekDay, Comparator.nullsLast(Comparator.naturalOrder()))
        .thenComparing(Event::getTime, Comparator.nullsLast(Comparator.naturalOrder()));

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        var eventStep = SCENARIO_STEP.getValue(requestContext, EventScenarioStep.class);

        switch (eventStep) {
            case INIT -> {
                SCENARIO_STEP.setValue(requestContext, EventScenarioStep.RESIDENT_SLIDER.name());

                userContextService.updateUserContext(user, requestContext);

                var upcomingEvents = eventRepository.findByCarryDateAfterOrCarryDateNull(Instant.now())
                    .stream().sorted(eventComparator).toList();

                if (upcomingEvents.isEmpty()) {
                    sendMessage(
                        requestContext,
                        MessageConstants.NO_FUTURE_EVENTS_FIND
                    );

                    return;
                }

                var event = upcomingEvents.get(INITIAL_EVENT_SLIDER_INDEX);

                var messageId = sendMessage(
                    requestContext,
                    bitrixFileClient.downloadHtml(event.getPreviewDescription().showUrl()),
                    MenuConstants.createEventSliderMenu(chatId, event)
                );

                SCENARIO_STEP.setValue(requestContext, SearchScenarioStep.RESIDENT_SLIDER.name());
                MESSAGE_ID.setValue(requestContext, messageId);
                SEARCH_METADATA.setValue(requestContext, new SearchMetadata("", INITIAL_EVENT_SLIDER_INDEX));

                userContextService.updateUserContext(user, requestContext);
            }
        }
    }

    @SneakyThrows
    public void slideTo(SlideDirection direction, Map<String, Object> context) {
        var chatId = CHAT_ID.getValue(context, Long.class);
        var user = userRepository.findByChatId(chatId);
        var messageId = MESSAGE_ID.getValue(context);

        var searchMetadata = SEARCH_METADATA.getValue(context, SearchMetadata.class);

        var upcomingEvents = eventRepository.findByCarryDateAfterOrCarryDateNull(Instant.now())
            .stream().sorted(eventComparator).toList();


        if (upcomingEvents.isEmpty()) {
            sendMessage(
                context,
                MessageConstants.NO_FUTURE_EVENTS_FIND
            );

            return;
        }

        var nextCursor = direction.nextPosition(searchMetadata.getSliderCursor(), upcomingEvents.size());

        var event = upcomingEvents.get(nextCursor);

        updateMessage(
            messageId,
            context,
            bitrixFileClient.downloadHtml(event.getPreviewDescription().showUrl()),
            MenuConstants.createEventSliderMenu(chatId, event)
        );

        searchMetadata.setSliderCursor(nextCursor);

        SEARCH_METADATA.setValue(context, searchMetadata);

        userContextService.updateUserContext(user, context);
    }

    public EventScenario(
        TelegramClientAdapter telegramClientAdapter,
        UserContextService userContextService,
        UserRepository userRepository,
        EventRepository eventRepository,
        BitrixFileClient bitrixFileClient
    ) {
        super(telegramClientAdapter);

        this.userContextService = userContextService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.bitrixFileClient = bitrixFileClient;
    }
}