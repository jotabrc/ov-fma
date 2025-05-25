package io.github.jotabrc.ov_fma_auth.config;

import io.github.jotabrc.ovauth.config.PropertiesWhitelistLoaderImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class LoadProperties {

    @PostConstruct
    public void init() throws Exception {
        new PropertiesWhitelistLoaderImpl().loadProperties("ov-auth.properties");
    }
}
