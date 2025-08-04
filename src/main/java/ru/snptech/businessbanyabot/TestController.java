package ru.snptech.businessbanyabot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixAuthService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final BitrixAuthService bitrixAuthService;

    @GetMapping
    public String test(
        @RequestParam("code") String code
    ) {

        bitrixAuthService.getValidAccessToken();

        log.warn(code);

        return code;
    }

}
