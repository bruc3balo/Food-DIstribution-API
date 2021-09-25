package com.api.fooddistribution.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JsonErrorResponse {
	private boolean success;
	private boolean has_error;
	private int api_code;
	private String api_code_description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String trx_id;
	private Object errors;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;

	public JsonErrorResponse() {
		super();
	}

	public JsonErrorResponse(boolean success, boolean has_error, int api_code, String api_code_description, String trx_id, Object errors) {
		super();
		this.success = success;
		this.has_error = has_error;
		this.api_code = api_code;
		this.api_code_description = api_code_description;
		this.trx_id = trx_id;
		this.errors = errors;
	}

}
