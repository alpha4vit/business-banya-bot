package ru.snptech.businessbanyabot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixGrowthLimit;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixNetworkingSphere;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixReadyToBeTeacher;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixSport;
import ru.snptech.businessbanyabot.integration.bitrix.properties.BitrixProperties;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixAuthService;
import ru.snptech.businessbanyabot.integration.bitrix.util.LabeledEnumUtil;
import ru.snptech.businessbanyabot.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserDetailsController {

    private final BitrixProperties bitrixProperties;
    private final BitrixAuthService bitrixAuthService;
    private final UserRepository userRepository;

    @GetMapping("/{chatId}/details")
    public String getHtmlPage(
        Model model,
        @PathVariable("chatId") String chatId
    ) {
        var user = userRepository.findByChatId(Long.parseLong(chatId));

        var sports = mapToString(user.getInfo().sports(), BitrixSport.class);

        log.error(sports);

        var networkingSphere = LabeledEnumUtil.fromId(BitrixNetworkingSphere.class, user.getInfo().networkingSphere()).getLabel();
        var growthLimit = LabeledEnumUtil.fromId(BitrixGrowthLimit.class, user.getInfo().growthLimit()).getLabel();
        var readyToBeTeacher = LabeledEnumUtil.fromId(BitrixReadyToBeTeacher.class, user.getInfo().readyToBeTeacher()).getLabel();
        var link = user.getInfo().photo().showUrl() + bitrixAuthService.getAuthPathParam();

        model.addAttribute("chatId", chatId);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("businessDescription", user.getInfo().businessDescription());
        model.addAttribute("mainActive", user.getInfo().mainActive());
        model.addAttribute("mainPassive", user.getInfo().mainPassive());
        model.addAttribute("interests", user.getInfo());
        model.addAttribute("avatarLink", bitrixProperties.getUrl() + link);
        model.addAttribute("sport", user.getInfo().sports().toString());
        model.addAttribute("principles", user.getInfo().principles().toString());
        model.addAttribute("music", user.getInfo().music());
        model.addAttribute("keywords", user.getInfo().keywords());
        model.addAttribute("favoriteMovies", user.getInfo().favoriteMovies());
        model.addAttribute("strengths", user.getInfo().strengths());
        model.addAttribute("achievements", user.getInfo().achievements());
        model.addAttribute("defeats", user.getInfo().defeats());
        model.addAttribute("teachers", user.getInfo().teachers());
        model.addAttribute("futureGoals", user.getInfo().futureGoals());
        model.addAttribute("networkingSphere", networkingSphere);
        model.addAttribute("growthLimit", growthLimit);
        model.addAttribute("readyToBeTeacher", readyToBeTeacher);

        return "user_details";
    }

    private <T extends Enum<T> & LabeledEnum> String mapToString(Object value, Class<T> enumClass) {
        List<Integer> ids = (List<Integer>) value;

        return ids.stream()
            .map(id -> LabeledEnumUtil.fromId(enumClass, String.valueOf(id)))
            .map(LabeledEnum::getLabel)
            .collect(Collectors.joining(", "));
    }
}
