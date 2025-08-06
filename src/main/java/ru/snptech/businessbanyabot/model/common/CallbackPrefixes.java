package ru.snptech.businessbanyabot.model.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CallbackPrefixes {

    public final class User {
        public static final String USER_CHOOSE_FAST_PAYMENT = "UCFP_";
        public static final String USER_CHOOSE_INVOICE_PAYMENT = "UCIP_";
        public static final String USER_DEPOSIT_FAST_PAYMENT = "UDFP_";
        public static final String USER_DEPOSIT_INVOICE_PAYMENT = "UDIP_";
    }

    public final class Admin {
        public static final String ADMIN_SURVEY_ACCEPT_PREFIX = "ASA_";
        public static final String ADMIN_SURVEY_DECLINE_PREFIX = "ASD_";

        public static final String ADMIN_SEND_NOTIFICATIONS = "ASN_";
    }

    public final class Slider {
        public static final String SLIDER_CARD_DETAILS = "SCD_";

        public static final String RESIDENT_SLIDER_NEXT_CARD_PREFIX = "RSNC_";
        public static final String RESIDENT_SLIDER_PREVIOUS_CARD_PREFIX = "RSPC_";

        public static final String EVENT_SLIDER_NEXT_CARD_PREFIX = "ESNC_";
        public static final String EVENT_SLIDER_PREVIOUS_CARD_PREFIX = "ESPC_";
    }

}
