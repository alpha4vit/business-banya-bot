package ru.snptech.businessbanyabot.repository.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.model.user.UserRole;

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

    public static Specification<TelegramUser> hasChildrenCount(Integer count) {
        return (root, query, builder) -> {
            Expression<Integer> childrenCountPath = root.get("info").get("childrenCount");

            if (count == null) {
                return builder.conjunction();
            }

            if (count == 4) {
                return builder.greaterThanOrEqualTo(childrenCountPath, 4);
            } else {
                return builder.equal(childrenCountPath, count);
            }
        };
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

    public static Specification<TelegramUser> hasEmployees(Integer min, Integer max) {
        return (root, query, builder) -> {
            Expression<Integer> employeesCountPath = root.get("info").get("ufEmployees");

            if (min == null && max == null) {
                return builder.conjunction();
            }

            return builder.between(employeesCountPath, min, max);
        };
    }

    public static Specification<TelegramUser> hasRole(UserRole role) {
        return (root, query, builder) ->
            builder.equal(root.get("role"), role.name());
    }

}
