package com.example.keycloak.config;

import com.example.keycloak.domain.Permission;
import com.example.keycloak.domain.Role;
import com.example.keycloak.endpoint_assess.EndpointAccess;
import com.example.keycloak.feature.menu.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final PermissionRepository permissionRepository;
    private final JwtAuthConverter jwtAuthConverter;

//    List<EndpointAccess> accessList = List.of(
//            // Products
//            new EndpointAccess("GET", "/api/products/**", "admin,hr"),
//            new EndpointAccess("POST", "/api/products/**", "admin"),
//            new EndpointAccess("DELETE", "/api/products/**", "admin"),
//            new EndpointAccess("PUT", "/api/products/**", "admin")

            // Categories
//            new EndpointAccess("GET", "/api/categories/**", "admin,hr"),
//            new EndpointAccess("POST", "/api/categories/**", "admin,staff"),
//            new EndpointAccess("DELETE", "/api/categories/**", "admin"),
//            new EndpointAccess("PUT", "/api/categories/**", "admin")
//    );


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        List<EndpointAccess> accessList = permissionRepository.findAll().stream()
                .map(permission -> EndpointAccess.builder()
                        .method(permission.getStatusMethod())
                        .pattern(permission.getPattern())
                        .roles(permission.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.joining(",")))
                        .build())
                .toList();

        Map<String, Map<String, List<String>>> grouped = accessList.stream()
                .collect(Collectors.groupingBy(
                        EndpointAccess::getMethod,
                        Collectors.groupingBy(
                                EndpointAccess::getPattern,
                                Collectors.mapping(
                                        access -> access.getRoles().trim(),
                                        Collectors.toList()
                                )
                        )
                ));

        // 1. diable CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        // 2. tells Spring Security that the app is acting as an OAuth2 Resource Server.
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        // 3. Stateless session ( no cookie , only jwt )
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 4. Set Access Rules for URLs
        http.authorizeHttpRequests(auth -> {
            grouped.forEach((method, patternMap) -> {
                patternMap.forEach((pattern, roles) -> {
                    auth.requestMatchers(HttpMethod.valueOf(method.toUpperCase()), pattern)
                            .hasAnyRole(roles.toArray(new String[0]));
                });
            });

            auth.anyRequest().authenticated();
        });

        // sample
//        http.authorizeHttpRequests(auth -> auth
//            // Products
//            .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("admin", "hr")
//            .requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("admin")
//            .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("admin")
//            .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole("admin")
//
//            // Categories
//            .requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyRole("admin", "hr")
//            .requestMatchers(HttpMethod.POST, "/api/categories/**").hasAnyRole("admin", "staff")
//            .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyRole("admin")
//            .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyRole("admin")
//
//            // Fallback
//            .anyRequest().authenticated()
//        );


        // 5. Enable JWT token ( Map JWT claims to Spring Security roles )
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
        );

        return http.build();
    }

}
