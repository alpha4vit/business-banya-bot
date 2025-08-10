package ru.snptech.businessbanyabot.integration.bitrix.mappers;

import lombok.experimental.UtilityClass;
import ru.snptech.businessbanyabot.entity.UserInfo;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyDto;

@UtilityClass
public class UserInfoMapper {

    public static UserInfo toInfo(final BitrixCompanyDto companyDto) {
        return UserInfo.builder()
                .title(companyDto.title())
                .hasPhone(companyDto.hasPhone())
                .hasEmail(companyDto.hasEmail())
                .hasImol(companyDto.hasImol())
                .assignedById(companyDto.assignedById())
                .createdById(companyDto.createdById())
                .modifyById(companyDto.modifyById())
                .industry(companyDto.industry())
                .revenue(companyDto.revenue())
                .currencyId(companyDto.currencyId())
                .employees(companyDto.employees())
                .dateCreate(companyDto.dateCreate())
                .dateModify(companyDto.dateModify())
                .opened(companyDto.opened())
                .isMyCompany(companyDto.isMyCompany())
                .lastActivityBy(companyDto.lastActivityBy())
                .lastActivityTime(companyDto.lastActivityTime())
                .ufEmployees(companyDto.ufEmployees())
                .residentStatus(companyDto.residentStatus())
                .residentCategory(companyDto.residentCategory())
                .birthDate(companyDto.birthDate())
                .entranceDate(companyDto.entranceDate())
                .balance(companyDto.balance())
                .activityCategory(companyDto.activityCategory())
                .city(companyDto.city())
                .businessStartYear(companyDto.businessStartYear())
                .businessDescription(companyDto.businessDescription())
                .averageMonthlyIncome(companyDto.averageMonthlyIncome())
                .familyStatus(companyDto.familyStatus())
                .childrenCount(companyDto.childrenCount())
                .sports(companyDto.sports())
                .principles(companyDto.principles())
                .music(companyDto.music())
                .keywords(companyDto.keywords())
                .favoriteMovies(companyDto.favoriteMovies())
                .strengths(companyDto.strengths())
                .achievements(companyDto.achievements())
                .defeats(companyDto.defeats())
                .teachers(companyDto.teachers())
                .futureGoals(companyDto.futureGoals())
                .mainActive(companyDto.mainActive())
                .mainPassive(companyDto.mainPassive())
                .annualTurnover(companyDto.annualTurnover())
                .businessClients(companyDto.businessClients())
                .recruitmentWays(companyDto.recruitmentWays())
                .networkingSphere(companyDto.networkingSphere())
                .growthLimit(companyDto.growthLimit())
                .readyToBeTeacher(companyDto.readyToBeTeacher())
                .photo(companyDto.photo())
                .phoneList(companyDto.phoneList())
                .build();
    }
}
