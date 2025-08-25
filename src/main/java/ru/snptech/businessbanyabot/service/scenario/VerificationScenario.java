package ru.snptech.businessbanyabot.service.scenario;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.ResidentStatus;
import ru.snptech.businessbanyabot.integration.bitrix.mappers.UserInfoMapper;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.businessbanyabot.integration.bitrix.util.LabeledEnumUtil;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.scenario.step.VerificationScenarioStep;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.propterties.ApplicationProperties;
import ru.snptech.businessbanyabot.repository.UserInfoRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.util.TimeUtils;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.time.Instant;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Slf4j
@Component
public class VerificationScenario extends AbstractScenario {

    private final BitrixIntegrationService bitrixIntegrationService;
    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final ApplicationProperties applicationProperties;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        if (UserRole.ADMIN.equals(user.getRole())) return;

        var isVerified = IS_VERIFIED.getValue(requestContext);

        if (Boolean.TRUE.equals(isVerified)) {
            if (SCENARIO.getValue(requestContext) == null) {
                SCENARIO.setValue(requestContext, ScenarioType.MAIN_MENU.name());

                userContextService.updateUserContext(user, requestContext);
            }

            return;
        }

        if (VerificationScenarioStep.WAITING_NUMBER.equals(SCENARIO_STEP.getValue(requestContext))) {
            verifyPhoneNumber(requestContext, user);

            return;
        }

        sendMessage(
            requestContext,
            MessageConstants.VERIFICATION_NEED_MESSAGE,
            MenuConstants.createVerificationMenu(applicationProperties.getPersonalDataConsentLink())
        );

        IS_VERIFIED.setValue(requestContext, false);
        SCENARIO_STEP.setValue(requestContext, VerificationScenarioStep.WAITING_NUMBER);
        userContextService.updateUserContext(user, requestContext);
    }

    protected void verifyPhoneNumber(Map<String, Object> context, TelegramUser user) {
        var message = TG_UPDATE.getValue(context).getMessage();
        var chatId = CHAT_ID.getValue(context, Long.class);

        if (!message.hasText() || !isPhoneNumber(message.getText())) {
            throw new BusinessBanyaDomainLogicException.PHONE_NUMBER_IS_REQUIRED();
        }

        var phoneNumber = message.getText();

        var existedUser = bitrixIntegrationService.findCompanyByPhoneNumber(phoneNumber);

        existedUser.ifPresent(
            (company) -> {
                var userInfo = UserInfoMapper.toInfo(chatId, company);

                user.setExternalId(company.id());
                user.setInfo(userInfo);
                user.setFullName(userInfo.getTitle());
                user.setSocialMedia(user.getTelegramUsername());
                user.setResidentUntil(
                    TimeUtils.plusMonths(
                        Instant.now(),
                        applicationProperties.getSubscriptionContinuationDurationInMonths()
                    )
                );

                var residentStatus = LabeledEnumUtil.fromId(ResidentStatus.class, userInfo.getResidentStatus());

                user.setRole(residentStatus.toUserRole());

                userInfoRepository.save(userInfo);
            }
        );

        user.setPhoneNumber(phoneNumber);
        IS_VERIFIED.setValue(context, true);
        SCENARIO.setValue(context, ScenarioType.MAIN_MENU.name());

        userContextService.updateUserContext(user, context);
    }

    private Boolean isPhoneNumber(String text) {
        return text != null && text.matches("^\\+\\d{10,15}$");
    }

    public VerificationScenario(
        BitrixIntegrationService bitrixIntegrationService,
        TelegramClientAdapter telegramClientAdapter,
        UserContextService userContextService,
        UserRepository userRepository,
        UserInfoRepository userInfoRepository,
        ApplicationProperties applicationProperties
    ) {
        super(telegramClientAdapter);
        this.bitrixIntegrationService = bitrixIntegrationService;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.applicationProperties = applicationProperties;
    }
}
