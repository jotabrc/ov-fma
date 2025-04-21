package io.github.jotabrc.ov_fma_gateway.config;

import io.github.jotabrc.ovauth.config.PropertiesWhitelistLoaderImpl;
import org.springframework.stereotype.Component;

@Component
public class LoadProperties {

    public LoadProperties() {
        init();
    }

    private void init() {
        new PropertiesWhitelistLoaderImpl().loadProperties();
        new RouteConfig().loadProperties();
    }
}
