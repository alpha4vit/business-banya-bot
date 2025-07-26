package ru.snptech.businessbanyabot.integrations.bank.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignBankClientAuthorizationInterceptor implements RequestInterceptor {

    private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJmYmZmNzYyMzYxMzIwNDNlODVmMjUyNGFkNTk1MjRiOCJ9.gZtq3vwZWNiLUO5kC8i1lKyuLOX5u1Y94HOHC1jiIBqtHF8i2ZJn36YjR-VMILCLHy7e05aqEU_5hbQ9lFNsK4F3OYKwTadMvQB2hhB_bE2osgu8IbLDsKHLIgNxWes4Jf3KmIN_xwsA2CrvB2-9vCY-EgtkLTZBQgWBe0hNRVFl1ts9ayzBf-lIo2dW8rrzhDGLsU2hZEL3O9WAmKD3hWEPdxLUkCF0D7Ya2lQ7I7ltQ743QaHmdS-uPMz4wooZl0CmtaxtCpqvwjQlfiaPG8vF-Gz2aD4f-igMEWTNPPqVttDgtNzAcM8DmXSpoPfLOnz3TizUPhwjRMQR5GsEVY-yJFfkXqa-HykITAk84cB8DQb7yPNHEquIEcuZqvyrSBa1LYYL90POmWJg1kaM8JcJCBA_pJKwiLWmEDOsdd3cHyRbbXH_RfWUudY8TClvA8mHRnZyMnwIKCZgts8DY9Nj4AdGlXaJEFZegDH8g8ETiIKpLqBOyvPWgdTYFB12";

    @Override
    public void apply(final RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer %s", token);
    }
}
