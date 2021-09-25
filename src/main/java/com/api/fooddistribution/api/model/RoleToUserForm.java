package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class RoleToUserForm {

    @JsonProperty(value = "username")
    @NotBlank(message = "username required")
    private String username;

    @JsonProperty(value = "role_name")
    @NotBlank(message = "role name is required")
    private String roleName;

    public RoleToUserForm() {

    }
}
