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

    @JsonProperty(USERNAME)
    @NotBlank(message = "product owner required")
    private String username;

    @JsonProperty(LOCATION)
    @NotBlank(message = "location of product required")
    private String location;

    public ProductCreationFrom() {

    }

    public ProductCreationFrom(String productName, String productPrice, String productCategoryName, String image, String unit, String productDescription, String username,String location) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategoryName = productCategoryName;
        this.image = image;
        this.unit = unit;
        this.productDescription = productDescription;
        this.username = username;
        this.location = location;
    }
}
