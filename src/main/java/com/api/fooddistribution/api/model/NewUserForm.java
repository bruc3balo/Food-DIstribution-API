package com.api.fooddistribution.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NewUserForm {

    @NotBlank(message = "name is required")
    @JsonProperty(value = "name")
    private String name;

    @NotBlank(message = "username is required")
    @JsonProperty(value = "username")
    private String username;

    @NotBlank(message = "email is required")
    @JsonProperty(value = "email_address")
    private String emailAddress;

    @NotBlank(message = "password is required")
    @JsonProperty(value = "password")
    private String password;

    private String role;

    public NewUserForm() {
    }

    public NewUserForm(String name, String username, String emailAddress, String password, String role) {
        this.name = name;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
    }
}
