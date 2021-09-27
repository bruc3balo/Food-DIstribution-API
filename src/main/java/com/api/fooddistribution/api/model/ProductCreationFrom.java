package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProductCreationFrom {

    @JsonProperty(value = "product_name")
    @NotBlank(message = "product name required")
    private String productName;

    @JsonProperty(value = "product_price")
    @NotBlank(message = "product price required")
    private String productPrice;

    @JsonProperty(value = "product_category_name")
    @NotBlank(message = "product category required")
    private String productCategoryName;

    @JsonProperty(value = "image")
    @NotBlank(message = "image required")
    private String image;


    public ProductCreationFrom() {

    }

    public ProductCreationFrom(String productName, String productPrice, String productCategoryName, String image) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategoryName = productCategoryName;
        this.image = image;
    }
}
