package net.chrisrichardson.eventstore.examples.kanban.commonauth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(ignoreUnknownFields = false, prefix = "auth")
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
