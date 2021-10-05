package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class UserUpdateForm {
    @JsonProperty(value = NAMES)
    private String name;

    @JsonProperty(value = EMAIL_ADDRESS)
    private String emailAddress;

    @JsonProperty(value = PASSWORD)
    private String password;

    @JsonProperty(value = ROLE)
    private String role;

    @JsonProperty(value = PHONE_NUMBER)
    private String phoneNumber;

    @JsonProperty(value = ID_NUMBER)
    private String idNumber;

    @JsonProperty(value = BIO)
    private String bio;

    @JsonProperty(value = PREFERRED_WORKING_HOURS)
    private Map<String, String> preferredWorkingHours;

    @JsonProperty(value = SPECIALITIES)
    private List<String> specialities;

    public UserUpdateForm() {

    }

    public UserUpdateForm(String name, String emailAddress, String password, String role) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
    }

    public UserUpdateForm(String name, String emailAddress, String password, String role, String phoneNumber, String idNumber, String bio, Map<String, String> preferredWorkingHours, List<String> specialities) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.bio = bio;
        this.preferredWorkingHours = preferredWorkingHours;
        this.specialities = specialities;
    }
}
