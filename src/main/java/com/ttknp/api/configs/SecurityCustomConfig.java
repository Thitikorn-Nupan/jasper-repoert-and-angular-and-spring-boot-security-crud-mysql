package com.ttknp.api.configs;

import com.ttknp.security.custom.configs.jwt.JwtRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/// Note!! the class name as SecurityConfig not allow because my security service used
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityCustomConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityCustomConfig.class);
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityCustomConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    @Order(2) 
    public SecurityFilterChain filterChainCustom(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuring filterChainCustom");
        // Test work after run auth micro
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity
                .csrf()
                .disable();
        // All req authen
        httpSecurity.securityMatcher("/gadget/**") // This chain only matches /server/**
                .cors(cors -> cors.configurationSource(corsSecureConfig())) // *** Custom cors config on security
                .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.GET,"/gadget/**").hasAuthority("admin");
                    authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.POST,"/gadget/**").hasAuthority("admin");
                    authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.POST,"/gadget/report").hasAuthority("admin");
                    authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.PUT,"/gadget/**").hasAuthority("admin");
                    authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.DELETE,"/gadget/**").hasAuthority("admin");
                    // Note , hasAuthority(...) will looking to string without prefix!!
                    authorizationManagerRequestMatcherRegistry.anyRequest().hasAuthority("admin");
                }).httpBasic();
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(this.jwtRequestFilter, BasicAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain filterChainCustom2(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuring filterChainCustom2");
        // Test work after run auth micro
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity
                .csrf()
                .disable();
        // All req authen
        httpSecurity.securityMatcher("/register/**") // This chain only matches /server/**
                .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.POST,"/register/login").permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                }).httpBasic();
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(this.jwtRequestFilter, BasicAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsSecureConfig() {
        log.debug("Configuring addCorsMappings (secure)");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // Allow specific origins (replace with your Angular app's URL)
        config.setAllowedOrigins(List.of("http://localhost:4200","http://thitikorn-nupan.com"));
        // Allow all methods (GET, POST, PUT, DELETE, etc.)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow all headers in the request
        config.setAllowedHeaders(Collections.singletonList("*")); // Note, set only Authorization won't work
        // **Crucially, expose the headers Angular needs to read**
        config.setExposedHeaders(Arrays.asList("Authorization", "File-Name"));
        // Allow credentials (e.g., cookies, authorization headers)
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/gadget/**", config);
        return source;
    }
}