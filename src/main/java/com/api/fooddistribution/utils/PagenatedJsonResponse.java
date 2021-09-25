package com.api.fooddistribution.utils;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PagenatedJsonResponse {
    private boolean success;
    private boolean has_error;
    private int api_code;
    private String api_code_description;
    private String message;
    private String trx_id;
    private Object data;
    private MetaData meta;

    public PagenatedJsonResponse() {

    }

    public PagenatedJsonResponse(boolean success, boolean has_error, int api_code, String api_code_description, String message, String trx_id, Object data) {
        this.success = success;
        this.has_error = has_error;
        this.api_code = api_code;
        this.api_code_description = api_code_description;
        this.message = message;
        this.trx_id = trx_id;
        this.data = data;

    }

    public PagenatedJsonResponse(boolean success, boolean has_error, int api_code, String api_code_description, String message, String trx_id) {
        this.success = success;
        this.has_error = has_error;
        this.api_code = api_code;
        this.api_code_description = api_code_description;
        this.message = message;
        this.trx_id = trx_id;
    }
}

