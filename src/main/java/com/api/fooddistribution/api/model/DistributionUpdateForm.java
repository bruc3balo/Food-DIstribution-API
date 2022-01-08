package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import java.util.Map;

import static com.api.fooddistribution.global.GlobalVariables.*;

@Getter
@Setter
public class DistributionUpdateForm {

    @JsonProperty(ID)
    @NotNull(message = "missing id")
    private Long id;

    @JsonProperty(DELETED)
    private Boolean deleted;

    @JsonProperty(PAID)
    private Boolean paid;

    @JsonProperty(LAST_LOCATION)
    private String lastKnownLocation;

    @JsonProperty(STATUS)
    private Integer status;

    @JsonProperty(REMARKS)
    private Long remarks;

    @JsonProperty(PRODUCT_STATUS)
    private Map<String, Integer> productStatus;

    public DistributionUpdateForm() {

    }
}
