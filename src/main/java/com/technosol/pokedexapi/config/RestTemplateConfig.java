package com.technosol.pokedexapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration  // Indique que cette classe contient des configurations
public class RestTemplateConfig {

    @Bean  // Crée un Bean injectable partout dans l'application
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
