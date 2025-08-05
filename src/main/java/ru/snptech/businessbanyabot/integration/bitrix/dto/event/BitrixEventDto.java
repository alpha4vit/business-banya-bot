package ru.snptech.businessbanyabot.integration.bitrix.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.FileDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BitrixEventDto(
    @JsonProperty("ID")
    String id,

    @JsonProperty("TITLE")
    String title,

    @JsonProperty("UF_CRM_1742458786903")
    String carryDate,

    @JsonProperty("UF_CRM_1754335478118")
    String weekDay,

    @JsonProperty("UF_CRM_1743440819598")
    String speaker,

    @JsonProperty("UF_CRM_1754335207065")
    FileDto fullDescription,

    @JsonProperty("UF_CRM_1754335220238")
    FileDto previewDescription,

    @JsonProperty("UF_CRM_1745990408213")
    FileDto photo,

    @JsonProperty("UF_CRM_1754335598159")
    String registrationLink,

    @JsonProperty("UF_CRM_1754335612157")
    String tableLink
) {
}
