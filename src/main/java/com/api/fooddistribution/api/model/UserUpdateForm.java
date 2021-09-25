package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateForm {


    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "email_address")
    private String emailAddress;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "role")
    private String role;

    public UserUpdateForm() {

    }

    public UserUpdateForm(String name, String emailAddress, String password, String role) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
    }
}
