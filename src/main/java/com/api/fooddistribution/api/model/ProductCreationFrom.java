package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class ProductCreationFrom {

    @JsonProperty(PRODUCT_NAME)
    @NotBlank(message = "product name required")
    private String productName;

    @JsonProperty(PRODUCT_PRICE)
    @NotBlank(message = "product price required")
    private String productPrice;

    @JsonProperty(PRODUCT_CATEGORY_NAME)
    @NotBlank(message = "product category required")
    private String productCategoryName;

    @JsonProperty(IMAGE)
    @NotBlank(message = "image required")
    private String image;

    @JsonProperty(UNIT)
    @NotBlank(message = "unit required")
    private String unit;

    @JsonProperty(PRODUCT_DESCRIPTION)
    @NotBlank(message = "description required")
    private String productDescription;


    public ProductCreationFrom() {

    }

    public ProductCreationFrom(String productName, String productPrice, String productCategoryName, String image) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategoryName = productCategoryName;
        this.image = image;
    }
}
