package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class ProductUpdateForm {

    @JsonProperty(ID)
    @NotBlank(message = "Product id needed")
    private String id;

    @JsonProperty(value = PRODUCT_NAME)
    private String productName;

    @JsonProperty(value = PRODUCT_PRICE)
    private String productPrice;

    @JsonProperty(value = PRODUCT_CATEGORY_NAME)
    private String productCategoryName;

    @JsonProperty(value = IMAGE)
    private String image;

    @JsonProperty(UNIT)
    private String unit;

    @JsonProperty(PRODUCT_DESCRIPTION)
    private String productDescription;

    public ProductUpdateForm() {
    }

    public ProductUpdateForm(String productName, String productPrice, String productCategoryName, String image,String productDescription) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategoryName = productCategoryName;
        this.image = image;
        this.productDescription = productDescription;
    }


}
