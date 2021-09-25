package com.api.fooddistribution.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import static com.api.fooddistribution.utils.ConvertDate.formatDate;
import static com.api.fooddistribution.utils.ConvertDate.formatLocalDateTime;

public class DataOps {

    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss a";
    public static final DateTimeFormatter LDT_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter LD_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);


    public static List<String> filterRequestParams(HttpServletRequest request, List<String> knownParams) {
        Enumeration<String> query = request.getParameterNames();
        List<String> list = Collections.list(query);
        list.removeAll(knownParams);
        return list;
    }

    public static Date getNowFormattedDate() throws ParseException {
        return ConvertDate.formatDate(formatLocalDate(LocalDate.now()), DATE_PATTERN);
    }

    public static Date getNowFormattedDate(LocalDate date) throws ParseException {
        return ConvertDate.formatDate(formatLocalDate(date), DATE_PATTERN);
    }

    public static Date getNowFormattedFullDate() throws ParseException {
        return ConvertDate.formatDate(formatLocalDateTime(LocalDateTime.now()), TIMESTAMP_PATTERN);
    }

    public static Date getSpecificFormattedFullDate(LocalDateTime dateTime) throws ParseException {
        return ConvertDate.formatDate(formatLocalDateTime(dateTime), TIMESTAMP_PATTERN);
    }

    public static String formatLocalDate(LocalDate ld) {
        return LD_FORMATTER.format(ld);
    }

    public static String formatLocalDateTime(LocalDateTime ldt) {
        return LDT_FORMATTER.format(ldt);
    }


    public static SimpleGrantedAuthority getGrantedAuthorityRole(String role) {
        return new SimpleGrantedAuthority(role);
    }

    public static boolean isNumeric(String text) {
        return StringUtils.isNumeric(text);
    }


    public static String exactStringValue(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    public static BigDecimal strToBigDecimal(String value) {
        try {
            return (value == null || value.trim().isEmpty() ? new BigDecimal("0") : ((new BigDecimal(value.trim()))));
        } catch (Exception ex) {
            return null;
        }
    }


    public static Integer strToInteger(String value) {
        try {
            return (value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim()));
        } catch (Exception ex) {
            return null;
        }
    }


}
