package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateForm {
    @JsonProperty(value = "product_name")
    private String productName;

    @JsonProperty(value = "product_price")
    private String productPrice;

    @JsonProperty(value = "product_category_name")
    private String productCategoryName;

    @JsonProperty(value = "image")
    private String image;

    public ProductUpdateForm() {
    }

    public ProductUpdateForm(String productName, String productPrice, String productCategoryName, String image) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategoryName = productCategoryName;
        this.image = image;
    }
}
