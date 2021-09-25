package com.api.fooddistribution.utils;

import lombok.Getter;

@Getter
public enum ApiCode {

    FAILED(1000, "Failed"),
    SUCCESS(1010, "Success");

    private final int code;
    private final String description;

    ApiCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
