package ru.snptech.businessbanyabot.integration.bitrix.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class EventFields {

    public static final Map<String, String> EVENT_FILTER = Map.of(
        "CATEGORY_ID", "72",
        "!UF_CRM_1754335207065", "",
        "!UF_CRM_1754335220238", ""
    );

    public static final List<String> SELECT = List.of(
        "ID",
        "TITLE",
        "UF_CRM_1742458786903",
        "UF_CRM_1754335478118",
        "UF_CRM_1743440819598",
        "UF_CRM_1754335207065",
        "UF_CRM_1754335220238",
        "UF_CRM_1745990408213",
        "UF_CRM_1754335598159",
        "UF_CRM_1754335612157",
        "UF_CRM_1754989983689"
    );

}
