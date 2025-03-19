package ru.snptech.ritualbitrixbot.types;


import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.snptech.ritualbitrixbot.entity.Deal;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;

@UtilityClass
public class ServiceConstantHolder {
    // BITRIX RQ
    public static final MetaDataKey<String> BITRIX_RQ_DEAL_ID = new MetaDataKey<>("bitrixRqDealId", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_SOURCE = new MetaDataKey<>("bitrixRqSource", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_SOURCE_2 = new MetaDataKey<>("bitrixRqSource2", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_DEAL_TYPE = new MetaDataKey<>("bitrixRqDealType", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_COMMENT = new MetaDataKey<>("bitrixRqComment", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_CONTACT_NAME = new MetaDataKey<>("bitrixRqContactName", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_CONTACT_PHONE = new MetaDataKey<>("bitrixRqContactPhone", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_ADDRESS = new MetaDataKey<>("bitrixRqAddress", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_CITY = new MetaDataKey<>("bitrixRqCity", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_CUSTOMER_NAME = new MetaDataKey<>("bitrixRqCustomerName", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_DECEASED_SURNAME = new MetaDataKey<>("bitrixRqDeceasedSurname", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_REGION = new MetaDataKey<>("bitrixRqRegion", String.class);
    public static final MetaDataKey<String> BITRIX_RQ_PHONE = new MetaDataKey<>("bitrixRqPhone", String.class);
    // TELEGRAM
    public static final MetaDataKey<TelegramUser> AUTHENTICATED_USER = new MetaDataKey<>("authenticatedUser", TelegramUser.class);
    public static final MetaDataKey<String> CHAT_ID = new MetaDataKey<>("chatId", String.class);
    public static final MetaDataKey<Update> TG_UPDATE = new MetaDataKey<>("tgUpdate", Update.class);
    public static final MetaDataKey<Boolean> IS_ADMIN = new MetaDataKey<>("isAdmin", Boolean.class);
    public static final MetaDataKey<Boolean> HAS_USERNAME = new MetaDataKey<>("isAdmin", Boolean.class);
    public static final MetaDataKey<String> SCENARIO_STEP = new MetaDataKey<>("scenarioStep", String.class);
    public static final MetaDataKey<String> SCENARIO = new MetaDataKey<>("scenario", String.class);
    public static final MetaDataKey<String> USER_STATE = new MetaDataKey<>("userState", String.class);
    public static final MetaDataKey<String> CURRENT_DEAL_ID = new MetaDataKey<>("currentDealId", String.class);
    public static final MetaDataKey<Deal> DEAL = new MetaDataKey<>("deal", Deal.class);
}
