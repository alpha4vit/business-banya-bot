package ru.snptech.businessbanyabot.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.FileDto;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.PhoneDto;

import java.util.List;

@Data
@Entity
@Builder
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internal_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "has_phone")
    private String hasPhone;

    @Column(name = "has_email")
    private String hasEmail;

    @Column(name = "has_imol")
    private String hasImol;

    @Column(name = "assigned_by_id")
    private String assignedById;

    @Column(name = "created_by_id")
    private String createdById;

    @Column(name = "modify_by_id")
    private String modifyById;

    @Column(name = "industry")
    private String industry;

    @Column(name = "revenue")
    private String revenue;

    @Column(name = "currency_id")
    private String currencyId;

    @Column(name = "employees")
    private String employees;

    @Column(name = "date_create")
    private String dateCreate;

    @Column(name = "date_modify")
    private String dateModify;

    @Column(name = "opened")
    private String opened;

    @Column(name = "is_my_company")
    private String isMyCompany;

    @Column(name = "last_activity_by")
    private String lastActivityBy;

    @Column(name = "last_activity_time")
    private String lastActivityTime;

    @Column(name = "uf_employees")
    private String ufEmployees;

    @Column(name = "resident_status")
    private String residentStatus;

    @Column(name = "resident_category")
    private String residentCategory;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "entrance_date")
    private String entranceDate;

    @Column(name = "balance")
    private String balance;

    @Column(name = "activity_category")
    private String activityCategory;

    @Column(name = "city")
    private String city;

    @Column(name = "business_start_year")
    private String businessStartYear;

    @Column(name = "business_description")
    private String businessDescription;

    @Column(name = "average_monthly_income")
    private String averageMonthlyIncome;

    @Column(name = "family_status")
    private String familyStatus;

    @Column(name = "children_count")
    private String childrenCount;

    @Type(JsonType.class)
    @Column(name = "sports", columnDefinition = "TEXT")
    private Object sports;

    @Type(JsonType.class)
    @Column(name = "principles", columnDefinition = "TEXT")
    private Object principles;

    @Column(name = "music")
    private String music;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "favorite_movies")
    private String favoriteMovies;

    @Column(name = "strengths")
    private String strengths;

    @Column(name = "achievements")
    private String achievements;

    @Column(name = "defeats")
    private String defeats;

    @Column(name = "teachers")
    private String teachers;

    @Column(name = "future_goals")
    private String futureGoals;

    @Column(name = "main_active")
    private String mainActive;

    @Column(name = "main_passive")
    private String mainPassive;

    @Column(name = "annual_turnover")
    private String annualTurnover;

    @Column(name = "business_clients")
    private String businessClients;

    @Column(name = "recruitment_ways")
    private String recruitmentWays;

    @Column(name = "networking_sphere")
    private String networkingSphere;

    @Column(name = "growth_limit")
    private String growthLimit;

    @Column(name = "ready_to_be_teacher")
    private String readyToBeTeacher;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private FileDto photo;

    @Type(JsonType.class)
    @Column(name = "phone_list", columnDefinition = "jsonb")
    private List<PhoneDto> phoneList;
}
