package com.api.fooddistribution.api.model;

import com.api.fooddistribution.api.domain.Models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.api.fooddistribution.global.GlobalVariables.PRODUCT;

@Getter
@Setter
public class ProductDoneModel {
    @JsonProperty(PRODUCT)
    private Models.Product product;

    @JsonProperty("count")
    private boolean done;

    public ProductDoneModel() {
    }

    public ProductDoneModel(Models.Product product, boolean done) {
        this.product = product;
        this.done = done;
    }
}
