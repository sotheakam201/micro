package com.micro.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityRuleProperties securityRuleProperties;

    public SecurityConfig(SecurityRuleProperties securityRuleProperties) {
        this.securityRuleProperties = securityRuleProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> {
            // 1. Open endpoints
            auth.requestMatchers("/eureka/**").permitAll();
            // 2. Dynamically loop through YAML rules
            if (securityRuleProperties.getRules() != null) {

                for (SecurityRuleProperties.Rule rule : securityRuleProperties.getRules()) {
                    System.out.println("ddddddddddddddddddddd " + rule);
                    auth.requestMatchers(HttpMethod.valueOf(rule.getMethod()), rule.getPath())
                            .hasRole(rule.getRole());
                }
            }
            // 3. Default fallback
            auth.anyRequest().authenticated();
        });
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
