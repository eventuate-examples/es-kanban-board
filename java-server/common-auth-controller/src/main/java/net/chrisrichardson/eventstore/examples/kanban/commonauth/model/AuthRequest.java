package net.chrisrichardson.eventstore.examples.kanban.commonauth.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

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
