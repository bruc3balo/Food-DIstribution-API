package com.api.fooddistribution.config.security;

import com.api.fooddistribution.config.jwt.JWTUsernameAndPasswordAuthenticationFilter;
import com.api.fooddistribution.config.jwt.JwtTokenVerifier;
import com.api.fooddistribution.global.GlobalService;
import com.api.fooddistribution.global.GlobalVariables;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static com.api.fooddistribution.global.GlobalService.passwordEncoder;
import static com.api.fooddistribution.global.GlobalService.userDetailsService;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] WHITELIST = new String[]{
            GlobalVariables.contextPath + "/user/all",
            "/",
            "html",
            "index",
            "/css/*",
            "/js/*",
            "/swagger-ui.html",
            "/login"
    };


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(GlobalService.userDetailsService).passwordEncoder(GlobalService.passwordEncoder);
        auth.authenticationProvider(daoAuthenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTUsernameAndPasswordAuthenticationFilter(authenticationManager()))
                .addFilterAfter(new JwtTokenVerifier(), JWTUsernameAndPasswordAuthenticationFilter.class);

        //todo whitelist
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

/*
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails lindaUser = User.builder()
                .username("admin")
                .password(new BCryptPasswordEncoder(10).encode("admin"))
                .authorities(AppRolesEnum.ADMIN.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(lindaUser);

    }*/
}
