package ru.snptech.businessbanyabot.telegram;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.telegram.scenario.admin.AdminUpdateScenario;
import ru.snptech.businessbanyabot.telegram.scenario.user.UserCallbackScenario;
import ru.snptech.businessbanyabot.telegram.scenario.user.UserMainMenuScenario;

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

    public static InlineKeyboardMarkup createRegionSelectMenu(List<Region> regions) {
        return new InlineKeyboardMarkup(
                regions.stream()
                        .map(region -> new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text(region.getName())
                                                .callbackData(region.getId().toString())
                                                .build()
                                )
                        )
                        .toList()
        );
    }

    public static InlineKeyboardMarkup createAdminModerationMenu(TelegramUser userForModeration) {
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

    public static InlineKeyboardMarkup createRejectReasonsMenu(String dealId) {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text("Заказчик передумал")
                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_CUSTOMER_CHANGED_MIND + dealId)
                                        .build()
                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("На адресе был другой агент")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_ANOTHER_AGENT_ON_ADDRESS + dealId)
//                                        .build()
//                        ),
                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text("Не отвечают на звонки")
                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_NO_CALL_ANSWER + dealId)
                                        .build()
                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Не удалось связаться")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_NO_TOUCH + dealId)
//                                        .build()
//                        ),
                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text("Не устроила цена")
                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_BAD_PRICE + dealId)
                                        .build()
                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Нет денег")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_NO_MONEY + dealId)
//                                        .build()
//                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Отказались пока ехал")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_REFUSED_WHILE_TRANSFER + dealId)
//                                        .build()
//                        ),
                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text("Оформили сами в другой компании")
                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_ANOTHER_COMPANY + dealId)
                                        .build()
                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Оформили через знакомых")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_FRIENDS_HELP + dealId)
//                                        .build()
//                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Перебил другой агент")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_ANOTHER_AGENT_INTERRUPTED + dealId)
//                                        .build()
//                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Перебили в морге")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_MORGUE_INTERRUPTED + dealId)
//                                        .build()
//                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Приехал. Не пустили")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_DONT_LET_GO + dealId)
//                                        .build()
//                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Есть свой агент")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_OWN_AGENT + dealId)
//                                        .build()
//                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("3-е лицо")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_THIRD_PERSON + dealId)
//                                        .build()
//                        ),
                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text("Консультация")
                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_CONSULTATION + dealId)
                                        .build()
                        )
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Не устроила сумма доставки")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_BAD_DELIVERY_PRICE + dealId)
//                                        .build()
//                        ),
//                        new InlineKeyboardRow(
//                                InlineKeyboardButton.builder()
//                                        .text("Не устроило время доставки")
//                                        .callbackData(UserCallbackScenario.USER_DEAL_REJECT_BAD_DELIVERY_TIME + dealId)
//                                        .build()
//                        )
                ))
                .build();
    }

    public static ReplyKeyboard createUserMainMenu(boolean isRootAccount) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(new ArrayList<>());
        keyboardMarkup.setIsPersistent(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.getKeyboard().addAll(List.of(
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.REPORTS_COMMAND)),
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.STATISTICS_COMMAND)),
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.ACTIVE_COMMAND)),
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.CLOSED_COMMAND))
        ));
        if (isRootAccount) {
            keyboardMarkup.getKeyboard().add(
                    new KeyboardRow(new KeyboardButton(UserMainMenuScenario.PARTNERS))
            );
        }
        return keyboardMarkup;
    }

    public static ReplyKeyboard createPartnerEditMenu(boolean isRootAccount) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(new ArrayList<>());
        keyboardMarkup.setIsPersistent(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.getKeyboard().addAll(List.of(
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.REPORTS_COMMAND)),
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.STATISTICS_COMMAND)),
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.ACTIVE_COMMAND)),
                new KeyboardRow(new KeyboardButton(UserMainMenuScenario.CLOSED_COMMAND))
        ));
        if (isRootAccount) {
            keyboardMarkup.getKeyboard().add(
                    new KeyboardRow(new KeyboardButton(UserMainMenuScenario.PARTNERS))
            );
        }
        return keyboardMarkup;
    }

    public static ReplyKeyboard adminMainMenu() {
        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(new KeyboardRow(new KeyboardButton(AdminUpdateScenario.DELETE_PARTNER_FROM_REGION_COMMAND)))
                .build();
    }
}
