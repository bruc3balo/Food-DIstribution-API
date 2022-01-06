package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashMap;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class PurchaseCreationForm {

    @JsonProperty(BUYERS_ID)
    @NotBlank(message = "buyer id missing")
    private String buyerId;

    @JsonProperty(LOCATION)
    @NotBlank(message = "location is missing")
    private String location;

    @JsonProperty(ADDRESS)
    @NotBlank(message = "address is missing")
    private String address;

    @JsonProperty("products")
    @NotEmpty(message = "products missing")
    private final LinkedHashMap<String, Integer> product = new LinkedHashMap<>();

    public PurchaseCreationForm() {

    }
}
