package com.api.fooddistribution.config.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.api.fooddistribution.config.jwt.TokenHelper.JwtTokenVerify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtTokenVerifier extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        JwtTokenVerify(request, response, filterChain);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] NO_JTW_CHECK = new String[]{
                "/login",
                /*"/user/all"*/
        };
       return Arrays.asList(NO_JTW_CHECK).contains(request.getServletPath());
    }
}
