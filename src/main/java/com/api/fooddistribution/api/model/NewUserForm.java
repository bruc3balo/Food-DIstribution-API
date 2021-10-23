package com.api.fooddistribution.api.model;


import com.api.fooddistribution.api.validation.email.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class NewUserForm {

    @JsonProperty(UID)
    @NotBlank(message = "uid is null")
    private String uid;

    @NotBlank(message = "name is required")
    @JsonProperty(value = NAME)
    private String name;

    @NotBlank(message = "username is required")
    @JsonProperty(value = USERNAME)
    private String username;

    @NotBlank(message = "email is required")
    @JsonProperty(value = EMAIL_ADDRESS)
    @ValidEmail(message = "invalid email")
    private String emailAddress;

    @NotBlank(message = "password is required")
    @JsonProperty(value = PASSWORD)
    private String password;

    @JsonProperty(value = PHONE_NUMBER)
    private String phoneNumber = HY;

    @JsonProperty(value = ID_NUMBER)
    private String idNumber = HY;

    @JsonProperty(value = BIO)
    private String bio = HY;

    @JsonProperty(value = ROLE)
    private String role;


    public NewUserForm() {
    }

    public NewUserForm(String name, String username, String emailAddress, String password, String phoneNumber, String idNumber, String bio, String role) {
        this.name = name;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.bio = bio;
        this.role = role;
    }
}
