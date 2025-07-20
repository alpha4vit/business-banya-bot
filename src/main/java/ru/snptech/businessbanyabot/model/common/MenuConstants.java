package ru.snptech.businessbanyabot.model.common;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.model.user.Role;
import ru.snptech.businessbanyabot.service.scenario.common.UserCallbackScenario;
import ru.snptech.businessbanyabot.service.scenario.user.UserMainMenuScenario;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MenuConstants {
    public static InlineKeyboardMarkup createInitDealAmountMenu(String dealId) {
        return new InlineKeyboardMarkup(List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("Указать сумму заказа")
                    .callbackData(UserCallbackScenario.USER_DEAL_AMOUNT_ENTER + dealId)
                    .build()
            )
        ));
    }

    public static InlineKeyboardMarkup createInitDealAmountCashedMenu(String dealId) {
        return new InlineKeyboardMarkup(List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("Указать размер комиссии")
                    .callbackData(UserCallbackScenario.USER_DEAL_COMMISSION_ENTER + dealId)
                    .build()
            )
        ));
    }


    public static InlineKeyboardMarkup createAdminSurveyAcceptMenu(TelegramUser userForModeration) {
        return new InlineKeyboardMarkup(List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton.builder().text("✅ Принять").callbackData("AA_" + userForModeration.getChatId().toString()).build()
            ),
            new InlineKeyboardRow(
                InlineKeyboardButton.builder().text("❌ Отклонить").callbackData("AR_" + userForModeration.getChatId().toString()).build()
            )
        ));
    }

    public static InlineKeyboardMarkup createGetOrCantDealMenu(String dealId) {
        return new InlineKeyboardMarkup(List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("✅ Беру заказ")
                    .callbackData(UserCallbackScenario.USER_DEAL_APPROVE_CALLBACK_PREFIX + dealId)
                    .build()
            ),
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("❌ Не могу взять")
                    .callbackData(UserCallbackScenario.USER_DEAL_REJECT_CALLBACK_PREFIX + dealId).
                    build()
            )
        ));
    }

    public static InlineKeyboardMarkup funeralMenu(String dealId) {
        return new InlineKeyboardMarkup(List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("✅ Сделка успешна")
                    .callbackData(UserCallbackScenario.USER_DEAL_SECOND_APPROVE_CALLBACK_PREFIX + dealId)
                    .build()
            ),
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("Нужен номер телефона")
                    .callbackData(UserCallbackScenario.USER_DEAL_SECOND_PHONE_CALLBACK_PREFIX + dealId)
                    .build()
            ),
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("❌ Отказ")
                    .callbackData(UserCallbackScenario.USER_DEAL_SECOND_REJECT_CALLBACK_PREFIX + dealId)
                    .build()
            )
        ));
    }

    public static InlineKeyboardMarkup dealSecondMenu(String dealId) {
        return new InlineKeyboardMarkup(List.of(
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("✅ Сделка успешна")
                    .callbackData(UserCallbackScenario.USER_DEAL_SECOND_APPROVE_CALLBACK_PREFIX + dealId)
                    .build()
            ),
            new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("❌ Отказ")
                    .callbackData(UserCallbackScenario.USER_DEAL_SECOND_REJECT_CALLBACK_PREFIX + dealId)
                    .build()
            )
        ));
    }


    public static ReplyKeyboard createUserMainMenu(Role userRole) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(new ArrayList<>());
        keyboardMarkup.setIsPersistent(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.getKeyboard().addAll(List.of(
            new KeyboardRow(new KeyboardButton(UserMainMenuScenario.SEARCH)),
            new KeyboardRow(new KeyboardButton(UserMainMenuScenario.BALANCE)),
            new KeyboardRow(new KeyboardButton(UserMainMenuScenario.EVENTS))
        ));

        if (Role.NON_RESIDENT.equals(userRole)) {
            keyboardMarkup.getKeyboard().add(new KeyboardRow(new KeyboardButton(UserMainMenuScenario.REQUEST)));
        }

        return keyboardMarkup;
    }

    public static InlineKeyboardMarkup createAdminSurveyAcceptMenu(Survey survey) {
        return new InlineKeyboardMarkup(
            List.of(
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("✅ Принять").callbackData("AA_" + survey.getId().toString()).build()
                ),
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("❌ Отклонить").callbackData("AR_" + survey.getId().toString()).build()
                )
            ));
    }
}
