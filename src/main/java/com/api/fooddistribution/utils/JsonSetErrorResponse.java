package com.api.fooddistribution.utils;

public class JsonSetErrorResponse {
    public static JsonResponse setResponse(int code, String apiDescription, String trxId) {
       JsonResponse serviceResponse = new JsonResponse();
        boolean success = false;
        boolean hasError = true;
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(code);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        return serviceResponse;
    }

    public static JsonResponse setResponse(Integer apiCode, String apiDescription, String trxId, Object content) {

        JsonResponse serviceResponse = new JsonResponse();
        boolean success = false;
        boolean hasError = true;

        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        serviceResponse.setData(content);

        return serviceResponse;

    }

    public static JsonResponse setResponse(Integer apiCode, String apiDescription, String message, String trxId, Object content) {

        JsonResponse serviceResponse = new JsonResponse();
        boolean success = false;
        boolean hasError = true;

        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        serviceResponse.setData(content);
        serviceResponse.setMessage(message);

        return serviceResponse;

    }

    public static PagenatedJsonResponse setErrorResponse(int code, String apiDescription, String trxId) {
        PagenatedJsonResponse serviceResponse = new PagenatedJsonResponse();
        boolean success = false;
        boolean hasError = true;
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(code);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        return serviceResponse;
    }

    public static PagenatedJsonResponse setErrorResponse(int code, String apiDescription, String trxId, Object content) {
        PagenatedJsonResponse serviceResponse = new PagenatedJsonResponse();
        boolean success = false;
        boolean hasError = true;
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(code);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        serviceResponse.setData(content);

        return serviceResponse;
    }
}

