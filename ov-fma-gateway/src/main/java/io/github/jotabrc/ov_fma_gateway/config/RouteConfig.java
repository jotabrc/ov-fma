package io.github.jotabrc.ov_fma_gateway.config;

import io.github.jotabrc.ovauth.config.PropertiesWhitelistLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class RouteConfig implements PropertiesWhitelistLoader {

    public static Map<String, String> routeConfig = new HashMap<>();

    @Override
    public void loadProperties() {
        loadProperties("service.properties");
    }

    @Override
    public void loadProperties(String path) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
            Properties properties = new Properties();
            properties.load(stream);
            properties.forEach((k,v) -> routeConfig.put(k.toString(), v.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
