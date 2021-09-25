package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class RoleCreationForm {

    @JsonProperty(value = "name")
    @NotBlank(message = "required role name")
    private String name;

    @JsonProperty(value = "permissions")
    @NotEmpty(message = "permissions cannot be empty")
    private Set<String> allowedPermissions = new LinkedHashSet<>();

    public RoleCreationForm() {

    }

    public RoleCreationForm(String name, Set<String> allowedPermissions) {
        this.name = name;
        this.allowedPermissions = allowedPermissions;
    }



}
