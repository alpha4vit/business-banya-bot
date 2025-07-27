package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.survey.SurveyScenario;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.util.ImageUtil;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class UserUpdateScenario extends AbstractScenario {
    private final UserCallbackScenario userCallbackScenario;
    private final UserMainMenuScenario userMainMenuScenario;
    private final SurveyScenario surveyScenario;
    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final TelegramClientAdapter telegramClientAdapter;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        if (TG_UPDATE.getValue(requestContext).hasCallbackQuery()) {
            userCallbackScenario.invoke(requestContext);

            return;
        }

        if (!TG_UPDATE.getValue(requestContext).hasMessage()) {
            throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
        }

        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var currentScenario = SCENARIO.getValue(requestContext, ScenarioType.class);

        cleanContextIfNeeded(chatId, requestContext);

        switch (currentScenario) {
            case MAIN_MENU -> {
                userMainMenuScenario.invoke(requestContext);
            }

            case SURVEY -> {
                surveyScenario.invoke(requestContext);
            }

            case PAYMENT -> {
                if (USER_ROLE.getValue(requestContext, UserRole.class).equals(UserRole.NON_RESIDENT)) {
                    var image = ImageUtil.decodeBase64ToFile(QR_CODE.getValue(requestContext));

                    telegramClientAdapter.sendFile(chatId, image);
                }
            }

            default -> {
                // nothing for now
            }
        }
    }

    private void cleanContextIfNeeded(Long chatId, Map<String, Object> requestContext) {
        if (
            TG_UPDATE.getValue(requestContext).getMessage().hasText()
                && "/start".equals(TG_UPDATE.getValue(requestContext).getMessage().getText())
                && UserMainMenuScenario.MAIN_MENU_COMMANDS.contains(TG_UPDATE.getValue(requestContext).getMessage().getText())
        ) {
            var user = userRepository.findByChatId(chatId);

            userContextService.cleanUserContext(user);
        }
    }
}
