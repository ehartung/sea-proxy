package org.zalando.seaproxy.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oauth2")
public class Oauth2Properties {
    private List<Map<String, String>> routes = new ArrayList<>();

    public List<Map<String, String>> getRoutes() {
        return routes;
    }
}
