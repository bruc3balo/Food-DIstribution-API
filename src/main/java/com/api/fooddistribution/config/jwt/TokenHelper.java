package com.api.fooddistribution.config.jwt;

import com.api.fooddistribution.api.model.UsernameAndPasswordAuthenticationRequest;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class TokenHelper {

    private static final Algorithm myAlgorithm = Algorithm.HMAC256(secretJwt.getBytes());

    //Mostly Used
    public static Authentication attemptAuthenticationAuthFilter(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) {
        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());


            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void successAuthFilterAccessToken(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);

        log.info(request.getServletPath() + " login path");

        String accessToken = JWT.create().withSubject(authResult.getName()).withExpiresAt(accessTokenTime).withIssuer(request.getRequestURL().toString()).withClaim("authorities", authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).withJWTId("ACCESS").sign(myAlgorithm);
        String refreshToken = JWT.create().withSubject(authResult.getName()).withExpiresAt(refreshTokenTime).withIssuer(request.getRequestURL().toString()).withClaim("authorities", authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).withJWTId("REFRESH").sign(myAlgorithm);

        String authType = tokenPrefix;
        String bearerToken = authType + accessToken;

        response.addHeader(AUTHORIZATION, bearerToken);

        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", accessToken);
        //  tokens.put("refresh_token", refreshToken);
        tokens.put("auth_type", authType);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public static String refreshToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(myAlgorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);


            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("authorities").asArray(String.class);

            return JWT.create().withSubject(username).withExpiresAt(refreshTokenTime).withIssuer("/refresh").withClaim("authorities", Arrays.asList(roles)).withJWTId("REFRESH").sign(myAlgorithm);
        } catch (Exception e) {

            if (e instanceof TokenExpiredException) {
                DecodedJWT decodedJWT = JWT.decode(token);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("authorities").asArray(String.class);

                return JWT.create().withSubject(username).withExpiresAt(refreshTokenTime).withIssuer("/refresh").withClaim("authorities", Arrays.asList(roles)).withJWTId("REFRESH").sign(myAlgorithm);
            }

            e.printStackTrace();
            return null;
        }
    }

    public static void unsuccessfulAuthenticationAuthFilter(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed, AuthenticationFailureHandler handler) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());

        response.sendError(UNAUTHORIZED.value(), failed.getMessage());


        JsonResponse failResponse = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), failed.getMessage(), "");
        // handler.onAuthenticationFailure(request,response,failed);

        new ObjectMapper().writeValue(response.getOutputStream(), failResponse);
    }

    public static void JwtTokenVerify(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader(jwtAuthHeader);

            if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(tokenPrefix)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorizationHeader.replace(tokenPrefix, "");

            JWTVerifier verifier = JWT.require(myAlgorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("authorities").asArray(String.class);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response); //pass on to next filter

        } catch (Exception e) {
            errorOnJwt(response, e);
        }
    }

    public static void errorOnJwt(HttpServletResponse response, Exception e) throws IOException {
        Map<String, String> error = new HashMap<>();
        response.setStatus(FORBIDDEN.value());
        if (e instanceof TokenExpiredException) {
            error.put("error", "Token expired. Use refresh token or log in again");
        } else {
            error.put("error", e.getMessage());
        }
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }


/*
    public static void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {

            String authorizationHeader = request.getHeader(jwtAuthHeader);
            response.setContentType(APPLICATION_JSON_VALUE);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Refresh token is missing");
            }

            String token = authorizationHeader.substring("Bearer ".length());
            JWTVerifier verifier = JWT.require(myAlgorithm).build();


            DecodedJWT decodedJWT = verifier.verify(token);

            String username = decodedJWT.getSubject();
            Models.AppUser user = GlobalService.dataService.getAUser(username);

            Date sec10 = new Date(System.currentTimeMillis() + 10 * 1000);

            String access_token = JWT.create().withSubject(user.getUsername()).withExpiresAt(sec10).withIssuer(request.getRequestURL().toString()).withClaim("authorities", user.getRoles().stream().map(Models.AppRole::getName).collect(Collectors.toList())).sign(myAlgorithm);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);

            new ObjectMapper().writeValue(response.getOutputStream(), tokens);

        } catch (Exception e) {
            errorOnJwt(response, e);
        }

    }
*/


}
