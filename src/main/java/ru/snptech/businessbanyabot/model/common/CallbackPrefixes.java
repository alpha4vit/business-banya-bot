package ru.snptech.businessbanyabot.model.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CallbackPrefixes {

    public final class User {
        public static final String USER_CHOOSE_FAST_PAYMENT = "UCFP_";
        public static final String USER_CHOOSE_INVOICE_PAYMENT = "UCIP_";
    }

    public final class Admin {
        public static final String ADMIN_SURVEY_ACCEPT_PREFIX = "ASA_";
        public static final String ADMIN_SURVEY_DECLINE_PREFIX = "ASD_";
    }

    public final class Slider {
        public static final String SLIDER_CARD_DETAILS = "SCD_";
        public static final String SLIDER_NEXT_CARD_PREFIX = "SNC_";
        public static final String SLIDER_PREVIOUS_CARD_PREFIX = "SPC_";
    }

}
