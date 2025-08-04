package ru.snptech.businessbanyabot.model.common;


import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.model.search.SearchMetadata;
import ru.snptech.businessbanyabot.model.user.UserStatus;

@UtilityClass
public class ServiceConstantHolder {

    // TELEGRAM
    public static final MetaDataKey<String> CHAT_ID = new MetaDataKey<>("chatId", String.class);
    public static final MetaDataKey<Update> TG_UPDATE = new MetaDataKey<>("tgUpdate", Update.class);
    public static final MetaDataKey<UserStatus> USER_STATUS = new MetaDataKey<>("userStatus", UserStatus.class);
    public static final MetaDataKey<String> SCENARIO_STEP = new MetaDataKey<>("scenarioStep", String.class);
    public static final MetaDataKey<String> SCENARIO = new MetaDataKey<>("scenario", String.class);
    public static final MetaDataKey<Boolean> IS_VERIFIED = new MetaDataKey<>("isVerified", Boolean.class);
    public static final MetaDataKey<Boolean> IS_SURVEY_ACCEPTED = new MetaDataKey<>("isSurveyAccepted", Boolean.class);
    public static final MetaDataKey<Survey> LATEST_SURVEY = new MetaDataKey<>("latestSurvey", Survey.class);
    public static final MetaDataKey<PaymentType> PAYMENT_TYPE = new MetaDataKey<>("paymentType", PaymentType.class);
    public static final MetaDataKey<Integer> DEPOSIT_AMOUNT = new MetaDataKey<>("depositAmount", Integer.class);
    public static final MetaDataKey<SearchMetadata> SEARCH_METADATA = new MetaDataKey<>("searchMetadata", SearchMetadata.class);
    public static final MetaDataKey<Integer> MESSAGE_ID = new MetaDataKey<>("messageId", Integer.class);
}
