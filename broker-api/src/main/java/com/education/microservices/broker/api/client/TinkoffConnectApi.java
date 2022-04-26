package com.education.microservices.broker.api.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
public class TinkoffConnectApi {

    @Value("${secret.sbx.token}")
    private String token;

    @Bean
    public InvestApi buildInvestApi() {
        return InvestApi.createSandbox(token);
    }

}
