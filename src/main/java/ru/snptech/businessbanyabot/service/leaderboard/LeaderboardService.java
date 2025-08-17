package ru.snptech.businessbanyabot.service.leaderboard;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.propterties.ApplicationProperties;
import ru.snptech.businessbanyabot.repository.UserInfoRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.repository.specification.UserSpecification;

import java.util.List;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.CHAT_ID;

@Component
@RequiredArgsConstructor
public class LeaderboardService {

    private final static Integer PAGE_NUMBER = 0;
    private final static String FIELD_SORT = "info.points";

    private final ApplicationProperties applicationProperties;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    private Pageable pageRequest;

    @PostConstruct
    public void init() {
        pageRequest = PageRequest.of(
            PAGE_NUMBER,
            applicationProperties.getLeaderboardSize(),
            Sort.by(Sort.Direction.DESC, FIELD_SORT)
        );
    }

    public String getLeaderboard(Map<String, Object> context) {
        var chatId = CHAT_ID.getValue(context, Long.class);
        var user = userRepository.findByChatId(chatId);

        var users = userRepository.findAll(
            Specification.allOf(
                UserSpecification.hasRole(UserRole.RESIDENT),
                UserSpecification.hasPointsNotEmptyAndNotZero()
            ),
            pageRequest
        );

        return buildLeaderboard(user, users);
    }

    private String buildLeaderboard(TelegramUser user, List<TelegramUser> participants) {
        var builder = new StringBuilder();

        if (UserRole.RESIDENT.equals(user.getRole())) {
            var rank = userInfoRepository.getUserRank(user.getChatId());

            builder.append(
                MessageConstants.LEADERBOARD_HEADER.formatted(
                    rank,
                    user.getInfo().getPoints()
                )
            );
        }

        var position = 1;

        for (var participant : participants) {
            builder.append(
                MessageConstants.LEADERBOARD_ROW_TEMPLATE.formatted(
                    position,
                    participant.getFullName(),
                    participant.getPhoneNumber(),
                    participant.getInfo().getPoints()
                )
            );

            position += 1;
        }

        return builder.toString();
    }
}
