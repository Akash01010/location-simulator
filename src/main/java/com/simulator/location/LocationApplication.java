package com.simulator.location;

import com.google.maps.GeoApiContext;
import com.simulator.location.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class LocationApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationApplication.class, args);
    }

    @Bean
    public GeoApiContext getGeoContextBean(AppConfig appConfig) {
        return new GeoApiContext.Builder()
                .apiKey(appConfig.directionApiKey)
                .build();
    }
}
