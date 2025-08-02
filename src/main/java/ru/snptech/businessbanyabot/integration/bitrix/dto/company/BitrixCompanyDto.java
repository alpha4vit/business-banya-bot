package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.FileDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BitrixCompanyDto(
    @JsonProperty("ID")
    String id,

    @JsonProperty("TITLE")
    String title,

    @JsonProperty("HAS_PHONE")
    String hasPhone,

    @JsonProperty("HAS_EMAIL")
    String hasEmail,

    @JsonProperty("HAS_IMOL")
    String hasImol,

    @JsonProperty("ASSIGNED_BY_ID")
    String assignedById,

    @JsonProperty("CREATED_BY_ID")
    String createdById,

    @JsonProperty("MODIFY_BY_ID")
    String modifyById,

    @JsonProperty("INDUSTRY")
    String industry,

    @JsonProperty("REVENUE")
    String revenue,

    @JsonProperty("CURRENCY_ID")
    String currencyId,

    @JsonProperty("EMPLOYEES")
    String employees,

    @JsonProperty("DATE_CREATE")
    String dateCreate,

    @JsonProperty("DATE_MODIFY")
    String dateModify,

    @JsonProperty("OPENED")
    String opened,

    @JsonProperty("IS_MY_COMPANY")
    String isMyCompany,

    @JsonProperty("LAST_ACTIVITY_BY")
    String lastActivityBy,

    @JsonProperty("LAST_ACTIVITY_TIME")
    String lastActivityTime,

    @JsonProperty("UF_CRM_EMPLOYEES")
    String ufEmployees,

    @JsonProperty("UF_CRM_1740029712849")
    String residentStatus,

    @JsonProperty("UF_CRM_1744002909175")
    String residentCategory,

    @JsonProperty("UF_CRM_1741586457426")
    String birthDate,

    @JsonProperty("UF_CRM_1744003826501")
    String entranceDate,

    @JsonProperty("UF_CRM_1744062873266")
    String balance,

    @JsonProperty("UF_CRM_1751618553855")
    String activityCategory,

    @JsonProperty("UF_CRM_1753564350706")
    String city,

    @JsonProperty("UF_CRM_1753564549812")
    String businessStartYear,

    @JsonProperty("UF_CRM_1753564743659")
    String businessDescription,

    @JsonProperty("UF_CRM_1753564819600")
    String averageMonthlyIncome,

    @JsonProperty("UF_CRM_1753564972697")
    String familyStatus,

    @JsonProperty("UF_CRM_1753565034091")
    String childrenCount,

    @JsonProperty("UF_CRM_1753565978360")
    Object sports,

    @JsonProperty("UF_CRM_1753566020803")
    Object principles,

    @JsonProperty("UF_CRM_1753566191776")
    String music,

    @JsonProperty("UF_CRM_1753566483937")
    String keywords,

    @JsonProperty("UF_CRM_1753566506757")
    String favoriteMovies,

    @JsonProperty("UF_CRM_1753566529182")
    String strongSides,

    @JsonProperty("UF_CRM_1753566550751")
    String achievements,

    @JsonProperty("UF_CRM_1753566595468")
    String defeats,

    @JsonProperty("UF_CRM_1753566733417")
    String teachers,

    @JsonProperty("UF_CRM_1753566862458")
    String futureGoals,

    @JsonProperty("UF_CRM_1753567007871")
    String mainActive,

    @JsonProperty("UF_CRM_1753567018433")
    String mainPassive,

    @JsonProperty("UF_CRM_1753567192377")
    String annualTurnover,

    @JsonProperty("UF_CRM_1753567278281")
    String businessClients,

    @JsonProperty("UF_CRM_1753567364294")
    String recruitmentWays,

    @JsonProperty("UF_CRM_1753567456712")
    String networkingSphere,

    @JsonProperty("UF_CRM_1753567528920")
    String growthLimit,

    @JsonProperty("UF_CRM_1753567605826")
    String readyToBeTeacher,

    @JsonProperty("UF_CRM_1753626792826")
    FileDto photo,

    @JsonProperty("PHONE")
    List<PhoneDto> phoneList
) {


}
