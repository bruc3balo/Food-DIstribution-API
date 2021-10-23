package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class UserUpdateForm {

    @JsonProperty(value = NAMES)
    private String name;

    @JsonProperty(value = ROLE)
    private String role;

    @JsonProperty(value = PHONE_NUMBER)
    private String phoneNumber;

    @JsonProperty(value = ID_NUMBER)
    private String idNumber;

    @JsonProperty(value = BIO)
    private String bio;

    @JsonProperty(TUTORIAL)
    private Boolean tutorial;

    public UserUpdateForm() {

    }

    public UserUpdateForm(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public UserUpdateForm(String name, String role, String phoneNumber, String idNumber, String bio,Boolean tutorial) {
        this.name = name;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.bio = bio;
        this.tutorial = tutorial;
    }
}
