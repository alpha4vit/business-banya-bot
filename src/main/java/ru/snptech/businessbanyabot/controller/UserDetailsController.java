package ru.snptech.businessbanyabot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.snptech.businessbanyabot.repository.UserRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserDetailsController {

    private final UserRepository userRepository;

    @GetMapping("/{chatId}/details")
    public String getHtmlPage(
        Model model,
        @PathVariable("chatId") String chatId
    ) {
        var user = userRepository.findByChatId(Long.parseLong(chatId));

        model.addAttribute("chatId", chatId);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("businessDescription", user.getInfo().businessDescription());
        model.addAttribute("mainActive", user.getInfo().mainActive());
        model.addAttribute("mainPassive", user.getInfo().mainPassive());
        model.addAttribute("interests", user.getInfo());
        model.addAttribute("avatarLink", "https://prime-ural.bitrix24.ru/bitrix/components/bitrix/crm.company.show/show_file.php?auth=6d4d8f68007ac9280000560200000796000007aabd928d766fd9c5709d85acfd69fe95&ownerId=4516&fieldName=UF_CRM_1753626792826&dynamic=Y&fileId=301793");
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
        model.addAttribute("networkingSphere", user.getInfo().networkingSphere());
        model.addAttribute("growthLimit", user.getInfo().growthLimit());
        model.addAttribute("readyToBeTeacher", user.getInfo().readyToBeTeacher());
        return "user_details";
    }

}
