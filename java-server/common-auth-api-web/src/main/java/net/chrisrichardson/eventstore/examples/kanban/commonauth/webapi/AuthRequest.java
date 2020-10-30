package net.chrisrichardson.eventstore.examples.kanban.commonauth.webapi;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AuthRequest {

  @NotBlank
  @Email
  private String email;

  public AuthRequest() {
  }

  public AuthRequest(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
