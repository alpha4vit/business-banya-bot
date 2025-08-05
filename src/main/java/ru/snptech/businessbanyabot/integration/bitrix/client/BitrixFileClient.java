package ru.snptech.businessbanyabot.integration.bitrix.client;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integration.bitrix.properties.BitrixProperties;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixAuthService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BitrixFileClient {

    private final BitrixAuthService bitrixAuthService;
    private final BitrixProperties bitrixProperties;
    private final OkHttpClient client;

    public String downloadHtml(String url) throws IOException {
        Request request = new Request.Builder()
            .url(bitrixProperties.getUrl() + url + bitrixAuthService.getAuthPathParam())
            .get()
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Unexpected error while file downloading : " + response.message());
            }

            return response.body().string();
        }
    }

}
