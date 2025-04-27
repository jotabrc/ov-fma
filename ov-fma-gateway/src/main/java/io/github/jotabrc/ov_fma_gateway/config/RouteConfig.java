package io.github.jotabrc.ov_fma_gateway.config;

import io.github.jotabrc.ovauth.config.PropertiesWhitelistLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            properties.forEach((k,v) -> {
                String resolvedValue = resolveEnVar(v.toString());
                routeConfig.put(k.toString(), resolvedValue);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveEnVar(String value) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");
        Matcher matcher = pattern.matcher(value);
        StringBuilder resolvedValue = new StringBuilder();

        String env = null;
        while (matcher.find()) {
            if (matcher.group(1).equals("segment")) continue;
            env = System.getenv(matcher.group(1));
            if (env == null || env.trim().isEmpty())
                env = "";
            matcher.appendReplacement(resolvedValue, env);
        }
        matcher.appendTail(resolvedValue);
        return resolvedValue.toString();
    }
}
