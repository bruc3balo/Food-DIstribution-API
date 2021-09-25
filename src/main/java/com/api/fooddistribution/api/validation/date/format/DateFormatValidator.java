package com.api.fooddistribution.api.validation.date.format;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
    private static final Logger logger = LogManager.getLogger(DateFormatValidator.class);

    private String format;
    private int minDateCharacters;
    private int maxDateCharacters;

    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        format = constraintAnnotation.format();
        maxDateCharacters = constraintAnnotation.maxDateCharacters();
        minDateCharacters = constraintAnnotation.minDateCharacters();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty() || format == null || format.trim().isEmpty())
            return true;

        try {
//            String dateNumeric = "[-0-9: ]{10,16}";
            String dateNumeric = String.format("[-0-9: ]{%d,%d}", minDateCharacters, maxDateCharacters);

            Pattern pattern = Pattern.compile(dateNumeric);

            if (value == null || value.trim().isEmpty() || format == null || format.trim().isEmpty())
                return true;



            if(format.trim().length() < minDateCharacters || format.trim().length() > maxDateCharacters)
                return false;

            if(!pattern.matcher(value.trim()).matches())
                return false;

            SimpleDateFormat frmt = new SimpleDateFormat(format);
            frmt.setLenient(false);
            Date date = frmt.parse(value.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
