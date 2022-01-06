package com.api.fooddistribution.api.model;

import com.api.fooddistribution.api.domain.Models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.api.fooddistribution.global.GlobalVariables.PRODUCT;

@Getter
@Setter
public class ProductCountModel {
    @JsonProperty(PRODUCT)
    private Models.Product product;

    @JsonProperty("count")
    private Integer count;

    public ProductCountModel() {
    }

    public ProductCountModel(Models.Product product, Integer count) {
        this.product = product;
        this.count = count;
    }
}
