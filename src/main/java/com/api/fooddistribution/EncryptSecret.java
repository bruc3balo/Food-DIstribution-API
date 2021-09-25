package com.api.fooddistribution;

import com.api.fooddistribution.api.model.RoleCreationForm;
import com.api.fooddistribution.config.security.AppRolesEnum;
import com.api.fooddistribution.utils.ConvertDate;
import com.api.fooddistribution.utils.ConvertToJson;
import com.api.fooddistribution.utils.DataOps;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.api.fooddistribution.utils.DataOps.*;


public class EncryptSecret {


    public static void main(String[] args) throws ParseException {
        //secret
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        System.out.println(passwordEncoder.encode("secret"));
        System.out.println(passwordEncoder.matches("secret", "$2a$10$l8Ffz3g/z0FHOFj3Op58eOdwK0kEINCgr9bZcrjz65ZJjgTxQCTdO"));

        Date date = ConvertDate.formatDate(formatLocalDateTime(LocalDateTime.now()), TIMESTAMP_PATTERN); //2020-05-08 23:17:22 PM
        System.out.println(date);

        Date date1 = ConvertDate.formatDate(formatLocalDate(LocalDate.now()), DATE_PATTERN);//2020-05-08
        System.out.println(date1);


        Set<String> roles = Arrays.stream(AppRolesEnum.values()).map(Enum::name).collect(Collectors.toSet());
        System.out.println("role list "+roles);
        final int[] c = {0};
        roles.forEach(r -> {
            Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, r).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(r))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
            RoleCreationForm roleCreationForm = new RoleCreationForm(r, permissionsList);
            System.out.println("form " + Arrays.toString(c) + " "+ConvertToJson.setJsonString(roleCreationForm));
            c[0]++;
        });
    }


}
