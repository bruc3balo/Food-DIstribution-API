package com.api.fooddistribution.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Objects;

@Getter
@Setter
public class SellerStats {

    @JsonProperty("products")
    private final LinkedHashMap<String, Integer> products = new LinkedHashMap<>();

    @JsonProperty("month")
    private Integer month;

    public SellerStats(Integer month,LinkedHashMap<String, Integer> emptyProducts) {
        this.month = month;
        products.putAll(emptyProducts);
    }

    public void addProductToMonth(String key, int items) {
        products.entrySet().stream().filter(entry -> Objects.equals(key, entry.getKey())).findFirst().ifPresent(currentItems -> products.put(key, currentItems.getValue() + items));
    }

}
