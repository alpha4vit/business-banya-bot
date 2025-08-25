package ru.snptech.businessbanyabot.model.report;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ReportType implements LabeledEnum {
    FAMILY_STATUS("1", "Семейное положение", FamilyStatus.class),
    CHILDREN_COUNT("2", "Количество детей", ChildrenCount.class),
    CITY("3", "Город", null),
    BUSINESS_CLIENTS("4", "Клиенты", BusinessClients.class),
    RECRUITMENT_WAYS("5", "Способы привлечения клиентов", RecruitmentWays.class),
    GROWTH_LIMIT("6", "Ограничитель роста", GrowthLimit.class),
    EMPLOYEE_COUNT("7", "Количество сотрудников", EmployeeCount.class),
    BUTTONS_STATS("8", "Количество нажатий на кнопики меню", null);

    public static final List<ReportType> ALL = Arrays.stream(values()).toList();

    @Getter
    private final String id;

    private final String label;

    private final Class<? extends Enum<?>> enumClass;

    ReportType(String id, String label, Class<? extends Enum<?>> enumClass) {
        this.id = id;
        this.label = label;
        this.enumClass = enumClass;
    }

    public List<LabeledEnum> getEnumValues() {
        if (enumClass == null) {
            return null;
        }

//        if (!enumClass.equals(LabeledEnum.class)) {
//            throw new IllegalStateException("Ошибка создания report type, неверный enum");
//        }

        return Arrays.stream(enumClass.getEnumConstants())
            .map(e -> (LabeledEnum) e)
            .toList();
    }

}
