package com.api.fooddistribution.utils;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class JsonSetSuccessResponse {
    public static JsonResponse setResponse(int apiCode, String apiDescription, String trxId, Object content) {
        JsonResponse serviceResponse = new JsonResponse();
        boolean success = true;
        boolean hasError = false;
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        serviceResponse.setData(content);
        return serviceResponse;
    }

    public static JsonResponse setResponse(Integer apiCode, String apiDescription,
                                           String trxId) {

        JsonResponse serviceResponse = new JsonResponse();
        boolean success = true;
        boolean hasError = false;

        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        return serviceResponse;

    }


    public static JsonResponse setResponseNoContent(int apiCode, String apiDescription, String trxId) {
        JsonResponse serviceResponse = new JsonResponse();
        boolean success = true;
        boolean hasError = false;
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        return serviceResponse;
    }
    
   public static PagenatedJsonResponse setPagenatedResponse(int apiCode, String apiDescription, String trxId, Page<?> content) {
        PagenatedJsonResponse serviceResponse = new PagenatedJsonResponse();
        boolean success = true;
        boolean hasError = false;
        List emptyList = Collections.emptyList();
        MetaData meta = new MetaData();
        meta.setFirst(content.isFirst());
        meta.setLast(content.isLast());
        meta.setNumber(Integer.valueOf(content.getNumber()));
        meta.setNumberOfElements(Integer.valueOf(content.getNumberOfElements()));
        meta.setTotalPages(Integer.valueOf(content.getTotalPages()));
        meta.setTotalElements(Long.valueOf(content.getTotalElements()));
        meta.setSize(Integer.valueOf(content.getSize()));
        meta.setSort(content.getSort());
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        if (content.hasContent()) {
            serviceResponse.setData(content.getContent());
            serviceResponse.setMeta(meta);
        } else {
            serviceResponse.setData(emptyList);
            serviceResponse.setMeta(null);
        }
        return serviceResponse;
    }

    public static PagenatedJsonResponse setPagenatedResponse(int apiCode, String apiDescription, String trxId, Page<?> content, Object data) {
        PagenatedJsonResponse serviceResponse = new PagenatedJsonResponse();
        boolean success = true;
        boolean hasError = false;
        List emptyList = Collections.emptyList();
        MetaData meta = new MetaData();
        meta.setFirst(content.isFirst());
        meta.setLast(content.isLast());
        meta.setNumber(Integer.valueOf(content.getNumber()));
        meta.setNumberOfElements(Integer.valueOf(content.getNumberOfElements()));
        meta.setTotalPages(Integer.valueOf(content.getTotalPages()));
        meta.setTotalElements(Long.valueOf(content.getTotalElements()));
        meta.setSize(Integer.valueOf(content.getSize()));
        meta.setSort(content.getSort());
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        System.out.println("Checking");
        if (content.hasContent()) {
            serviceResponse.setData(data);
            serviceResponse.setMeta(meta);
        } else {
            serviceResponse.setData(emptyList);
            serviceResponse.setMeta(null);
        }
        return serviceResponse;
    }

    public static PagenatedJsonResponse setCustomPagenatedResponse(int apiCode, String apiDescription, String trxId, Page<?> pageObj, Object obj) {
        PagenatedJsonResponse serviceResponse = new PagenatedJsonResponse();
        boolean success = true;
        boolean hasError = false;
        List emptyList = Collections.emptyList();
       MetaData meta = new MetaData();
        meta.setFirst(pageObj.isFirst());
        meta.setLast(pageObj.isLast());
        meta.setNumber(Integer.valueOf(pageObj.getNumber()));
        meta.setNumberOfElements(Integer.valueOf(pageObj.getNumberOfElements()));
        meta.setTotalPages(Integer.valueOf(pageObj.getTotalPages()));
        meta.setTotalElements(Long.valueOf(pageObj.getTotalElements()));
        meta.setSize(Integer.valueOf(pageObj.getSize()));
        meta.setSort(pageObj.getSort());
        serviceResponse.setSuccess(success);
        serviceResponse.setHas_error(hasError);
        serviceResponse.setApi_code(apiCode);
        serviceResponse.setApi_code_description(apiDescription);
        serviceResponse.setTrx_id(trxId);
        System.out.println("Checking");
        if (pageObj.hasContent()) {
            serviceResponse.setData(obj);
            serviceResponse.setMeta(meta);
        } else {
            serviceResponse.setData(emptyList);
            serviceResponse.setMeta(null);
        }
        return serviceResponse;
    }

    public static Page<Object> defaultPage(){
        return new Page<Object>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super Object, ? extends U> function) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<Object> getContent() {
                return null;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<Object> iterator() {
                return null;
            }
        };
    }

}

