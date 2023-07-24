package com.example.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "pubsub")
@Data
public class PubSubProperties {

    private Map<String, String> topics;
    
}
