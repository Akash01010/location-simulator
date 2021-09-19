package com.simulator.location.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class AppConfig {

    @Value("${DIRECTION_API_KEY}")
    public String directionApiKey;
}
