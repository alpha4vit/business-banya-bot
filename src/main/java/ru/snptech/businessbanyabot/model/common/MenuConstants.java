package ru.snptech.businessbanyabot.model.common;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import ru.snptech.businessbanyabot.entity.Event;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.service.scenario.admin.AdminMainMenuScenario;
import ru.snptech.businessbanyabot.service.scenario.user.UserMainMenuScenario;

import java.util.ArrayList;
import java.util.List;

import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Admin.*;
import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Slider.*;
import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.User.*;

@UtilityClass
public class MenuConstants {


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

    public static ReplyKeyboard createUserMainMenu(UserRole userRole) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(new ArrayList<>());
        keyboardMarkup.setIsPersistent(true);
        keyboardMarkup.setResizeKeyboard(true);

        if (UserRole.NON_RESIDENT.equals(userRole)) {
            keyboardMarkup.getKeyboard().add(new KeyboardRow(new KeyboardButton(UserMainMenuScenario.REQUEST)));
        } else {
            keyboardMarkup.getKeyboard().addAll(List.of(
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.SEARCH)),
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.BALANCE)),
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.EVENTS))
            ));
        }

        return keyboardMarkup;
    }

    public static ReplyKeyboard createAdminMainMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(new ArrayList<>());
        keyboardMarkup.setIsPersistent(true);
        keyboardMarkup.setResizeKeyboard(true);

        keyboardMarkup.getKeyboard().addAll(List.of(
            new KeyboardRow(new KeyboardButton(AdminMainMenuScenario.NOTIFICATION)),
            new KeyboardRow(new KeyboardButton(AdminMainMenuScenario.PAYMENT_REPORT))
        ));

        return keyboardMarkup;
    }

    public static InlineKeyboardMarkup createAdminSurveyAcceptMenu(Long surveyId) {
        return new InlineKeyboardMarkup(
            List.of(
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("✅ Принять").callbackData(ADMIN_SURVEY_ACCEPT_PREFIX + surveyId).build()
                ),
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("❌ Отклонить").callbackData(ADMIN_SURVEY_DECLINE_PREFIX + surveyId).build()
                )
            ));
    }

    public static InlineKeyboardMarkup createChoosePaymentMethodMenu(Long chatId) {
        return new InlineKeyboardMarkup(
            List.of(
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("Оплата через СБП QR").callbackData(USER_CHOOSE_FAST_PAYMENT + chatId).build()
                ),
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("Выставление счета для Юр. лица").callbackData(USER_CHOOSE_INVOICE_PAYMENT + chatId).build()
                )
            ));
    }

    public static InlineKeyboardMarkup createChooseDepositMethodMenu(Long chatId) {
        return new InlineKeyboardMarkup(
            List.of(
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("Пополнение через СБП QR").callbackData(USER_DEPOSIT_FAST_PAYMENT + chatId).build()
                ),
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("Выставление счета для Юр. лица").callbackData(USER_DEPOSIT_INVOICE_PAYMENT + chatId).build()
                )
            ));
    }

    public static InlineKeyboardMarkup createResidentSliderMenu(Long chatId, Long cardUserChatId) {
        String url = "https://businessbanyasnp.duckdns.org/users/%s/details".formatted(cardUserChatId);

        InlineKeyboardButton webAppButton = InlineKeyboardButton.builder()
            .text("Подробнее")
            .webApp(new WebAppInfo(url))
            .build();

        return new InlineKeyboardMarkup(
            List.of(
                new InlineKeyboardRow(webAppButton),
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("◀").callbackData(RESIDENT_SLIDER_PREVIOUS_CARD_PREFIX + chatId).build(),
                    InlineKeyboardButton.builder().text("▶").callbackData(RESIDENT_SLIDER_PREVIOUS_CARD_PREFIX + chatId).build()
                )
            ));
    }

    public static InlineKeyboardMarkup createEventSliderMenu(Long chatId, Event event) {
        InlineKeyboardButton webAppButton = InlineKeyboardButton.builder()
            .text("Зарегистрироваться")
            .webApp(new WebAppInfo(event.getRegistrationLink()))
            .build();

        return new InlineKeyboardMarkup(
            List.of(
                new InlineKeyboardRow(webAppButton),
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("◀").callbackData(EVENT_SLIDER_PREVIOUS_CARD_PREFIX + chatId).build(),
                    InlineKeyboardButton.builder().text("▶").callbackData(EVENT_SLIDER_NEXT_CARD_PREFIX + chatId).build()
                )
            ));
    }

    public static InlineKeyboardMarkup createNotificationMenu(Long chatId) {
        return new InlineKeyboardMarkup(
            List.of(
                new InlineKeyboardRow(
                    InlineKeyboardButton.builder().text("Отправить").callbackData(ADMIN_SEND_NOTIFICATIONS + chatId).build()
                )
            ));
    }
}
