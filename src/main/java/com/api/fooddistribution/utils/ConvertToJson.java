package com.api.fooddistribution.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.simple.parser.JSONParser;

import java.math.BigDecimal;

public class ConvertToJson {
    public static String setJsonString(Object content) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writeValueAsString(content);
            return jsonInString;
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

//    public static String objectToJsonString(Object object) {
//        Gson gson = new GsonBuilder().create();
//        String gsonValue = gson.toJson(object);
//        return gsonValue;
//    }

    public static Object objectToJsonObject(Object object){
        try {
            String jsonString = setJsonString(object);
            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
            Gson gson = new GsonBuilder().create();
            String gsonValue = gson.toJson(jsonObject);
            JSONParser jsonParser=new JSONParser();
            return jsonParser.parse(gsonValue);
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public static String trimedString(String str) {
        return str == null || str.trim().isEmpty() ? str : str.trim();
    }

    public static String stringValue(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim().toUpperCase();
    }

    public static BigDecimal exactAmount(String amount){
        return (amount == null || amount.trim().isEmpty() ? new BigDecimal("0") : ((new BigDecimal(amount))));
    }
}

