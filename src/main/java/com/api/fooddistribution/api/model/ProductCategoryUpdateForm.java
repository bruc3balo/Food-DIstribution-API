package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class ProductCategoryUpdateForm {
    @JsonProperty(NAME)
    private String name;

    @JsonProperty(DELETED)
    private Boolean deleted;

    @JsonProperty(DISABLED)
    private Boolean disabled;
}
