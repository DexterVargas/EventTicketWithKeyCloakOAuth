package com.dexterv.eventticket.config;

import com.dexterv.eventticket.filters.UserProvisioningFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    public SecurityFilterChain filterChain (
//            HttpSecurity http,
//            UserProvisioningFilter userProvisioningFilter
//    ) throws Exception {
//        http
//                .authorizeHttpRequests(authorize ->
//                        // All Request must be authenticated
//                        authorize.anyRequest().authenticated())
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2ResourceServer(oauth2ResourceServer ->
//                        oauth2ResourceServer.jwt(
//                                Customizer.withDefaults()
//                        ))
//                .addFilterAfter(userProvisioningFilter, BearerTokenAuthenticationFilter.class);
//
//        return http.build();
//    }

    // To make the endpoints accessible without authentication
    public SecurityFilterChain filterChain (
            HttpSecurity http,
            UserProvisioningFilter userProvisioningFilter
    ) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                // Allow public access to published events
                                .requestMatchers(HttpMethod.GET, "/api/v1/published-events/**").permitAll()
                                // All other endpoints require authentication
                                .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(
                                Customizer.withDefaults()
                        ))
                .addFilterAfter(userProvisioningFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
