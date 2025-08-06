package ru.snptech.businessbanyabot.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.snptech.businessbanyabot.entity.TelegramUser;

import java.util.Collection;
import java.util.List;

public class TelegramUserSpecification {

    public static Specification<TelegramUser> nameContainsAny(Collection<String> patterns) {
        return (root, query, cb) -> {
            List<Predicate> likePredicates = patterns.stream()
                .map(p -> cb.like(cb.lower(root.get("fullName")), "%" + p.toLowerCase() + "%"))
                .toList();

            return cb.or(likePredicates.toArray(new Predicate[0]));
        };
    }

}
