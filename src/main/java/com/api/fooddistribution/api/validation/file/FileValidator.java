package com.api.fooddistribution.api.validation.file;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile[]> {
    private static String FILE_EXTN_PATTERN = "([\\w  -.@^:}]+(\\.(?i)(jpg|png|jpeg|pdf|xlsx|xls|doc|docx))$)";
    private static Pattern pattern;
    private static Matcher matcher;

    @Override
    public void initialize(ValidFile constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile[] value, ConstraintValidatorContext context) {
        if (value == null || value.length == 0)
            return true;
        return isValidFileExtension(value);
    }

    public static boolean isValidFileExtension(MultipartFile[]  files){
        pattern = Pattern.compile(FILE_EXTN_PATTERN, Pattern.CASE_INSENSITIVE);
        boolean fileValid = false;
        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "");
                System.out.println("File Name ::: " + fileName);
                matcher = pattern.matcher(fileName);
                fileValid = matcher.matches();
            }
        }
        catch (Exception e) {
//            e.printStackTrace();
        }
        return fileValid;
    }
}
