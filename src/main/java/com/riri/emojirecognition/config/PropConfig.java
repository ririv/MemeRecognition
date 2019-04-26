package com.riri.emojirecognition.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix="classifier")
public class PropConfig {
    private String modelResourcePath;

    public String getModelResourcePath() {
        return modelResourcePath;
    }

    public void setModelResourcePath(String modelResourcePath) {
        this.modelResourcePath = modelResourcePath;
    }
}
