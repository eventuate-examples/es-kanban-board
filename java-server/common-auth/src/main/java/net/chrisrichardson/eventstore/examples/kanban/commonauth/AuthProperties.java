package net.chrisrichardson.eventstore.examples.kanban.commonauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(locations = "classpath:auth.properties", ignoreUnknownFields = false, prefix = "auth")
public class AuthProperties {
    private String serverSecret;
    private Integer serverInteger;

    public String getServerSecret() {
        return serverSecret;
    }

    public void setServerSecret(String serverSecret) {
        this.serverSecret = serverSecret;
    }

    public Integer getServerInteger() {
        return serverInteger;
    }

    public void setServerInteger(Integer serverInteger) {
        this.serverInteger = serverInteger;
    }
}
