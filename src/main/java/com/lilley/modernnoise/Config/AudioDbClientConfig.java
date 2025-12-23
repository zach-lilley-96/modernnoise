package com.lilley.modernnoise.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AudioDbClientConfig {
    @Value("${app.music.api.url}")
    public String baseApiUrl;

    @Bean
    RestClient audioDbClient() {
        return RestClient.builder()
                .baseUrl(baseApiUrl)
                .defaultHeader("Accept", "application/json/")
                .build();
    }

}
