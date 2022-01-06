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

    @JsonProperty(DISABLED)
    private Boolean disabled;

    @JsonProperty(DELETED)
    private Boolean deleted;

    @JsonProperty(UNITS_LEFT)
    private Double unitsLeft;


    public ProductUpdateForm(String id, Double unitsLeft) {
        this.id = id;
        this.unitsLeft = unitsLeft;
    }

    public ProductUpdateForm() {
    }

    public ProductUpdateForm(String productName, String productPrice, String productCategoryName, String image, String productDescription) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategoryName = productCategoryName;
        this.image = image;
        this.productDescription = productDescription;
    }

    public ProductUpdateForm(String id, String productName, String productPrice, String productCategoryName, String image, String unit, String productDescription, Boolean disabled, Boolean deleted) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategoryName = productCategoryName;
        this.image = image;
        this.unit = unit;
        this.productDescription = productDescription;
        this.disabled = disabled;
        this.deleted = deleted;
    }
}
