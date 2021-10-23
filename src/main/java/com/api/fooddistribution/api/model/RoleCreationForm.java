package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.api.fooddistribution.global.GlobalVariables.NAME;
import static com.api.fooddistribution.global.GlobalVariables.PERMISSIONS;

@Getter
@Setter
public class RoleCreationForm {

    @JsonProperty(value = NAME)
    @NotBlank(message = "required role name")
    private String name;

    @JsonProperty(value = PERMISSIONS)
    @NotEmpty(message = "permissions cannot be empty")
    private Set<String> permissions = new LinkedHashSet<>();

    public RoleCreationForm() {

    }

    public RoleCreationForm(String name, Set<String> permissions) {
        this.name = name;
        this.permissions = permissions;
    }



}
