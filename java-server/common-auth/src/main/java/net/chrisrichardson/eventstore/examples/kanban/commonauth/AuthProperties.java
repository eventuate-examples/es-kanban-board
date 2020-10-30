package net.chrisrichardson.eventstore.examples.kanban.commonauth;

import org.springframework.beans.factory.annotation.Value;

public class AuthProperties {
  @Value("${auth.server.secret}")
  private String serverSecret;

  @Value("${auth.server.integer}")
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
