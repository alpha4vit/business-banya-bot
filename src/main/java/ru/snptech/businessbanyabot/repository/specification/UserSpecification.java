package ru.snptech.businessbanyabot.repository.specification;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.ManagedType;
import org.springframework.data.jpa.domain.Specification;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyDto;

import java.util.Collection;
import java.util.List;

public class UserSpecification {

    public static Specification<TelegramUser> nameContainsAny(Collection<String> patterns) {
        return (root, query, cb) -> {
            List<Predicate> likePredicates = patterns.stream()
                .map(p -> cb.like(cb.lower(root.get("fullName")), "%" + p.toLowerCase() + "%"))
                .toList();

            return cb.or(likePredicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<TelegramUser> hasFamilyStatus(String familyStatus) {
        return (root, query, builder) ->
            builder.equal(root.get("info").get("familyStatus"), familyStatus);
    }

    public static Specification<TelegramUser> hasChildrenCount(String count) {
        return (root, query, builder) ->
            builder.equal(root.get("info").get("childrenCount"), count);
    }

    public static Specification<TelegramUser> hasBusinessClients(String client) {
        return (root, query, builder) ->
            builder.equal(root.get("info").get("businessClients"), client);
    }

    public static Specification<TelegramUser> hasRecruitmentWays(String way) {
        return (root, query, builder) ->
            builder.equal(root.get("info").get("recruitmentWays"), way);
    }

    public static Specification<TelegramUser> hasGrowthLimit(String limit) {
        return (root, query, builder) ->
            builder.equal(root.get("info").get("growthLimit"), limit);
    }

    public static Specification<TelegramUser> hasUfEmployees(String employeesCount) {
        return (root, query, builder) ->
            builder.equal(root.get("info").get("ufEmployees"), employeesCount);
    }
}
