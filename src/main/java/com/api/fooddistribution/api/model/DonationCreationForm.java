package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedList;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class DonationCreationForm {

    @JsonProperty(DONOR)
    @NotBlank(message = "donor is missing")
    private String donor;

    @JsonProperty(BENEFICIARY)
    @NotBlank(message = "beneficiary is missing")
    private String beneficiary;

    @JsonProperty(DELIVERY_LOCATION)
    @NotBlank(message = "delivery location is missing")
    private String deliveryLocation;

    @JsonProperty(DELIVERY_ADDRESS)
    @NotBlank(message = "delivery address is missing")
    private String deliveryAddress;

    @JsonProperty(COLLECTION_LOCATION)
    @NotBlank(message = "collection location is missing")
    private String collectionLocation;

    @JsonProperty(COLLECTION_ADDRESS)
    @NotBlank(message = "collection address is missing")
    private String collectionAddress;

    @NotEmpty(message = "products cannot be empty")
    private LinkedList<DonorItem> products = new LinkedList<>();

    DonationCreationForm () {

    }
}
