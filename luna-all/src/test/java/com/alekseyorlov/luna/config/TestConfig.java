package com.alekseyorlov.luna.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("com.alekseyorlov.luna")
@EnableAutoConfiguration
@PropertySource(value = {"classpath:/orm-configuration.properties"})
@EntityScan({
        "com.alekseyorlov.luna.domain",
        // to activate JPA 2.1 converters
        "org.springframework.data.jpa.convert.threeten"
})
@Configuration
public class TestConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }
}
