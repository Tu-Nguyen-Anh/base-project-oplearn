package org.oplearn.project.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.constanst.OpLearnConstants;
import org.oplearn.project.filter.JwtAuthenticationFilter;
import org.oplearn.project.security.error.UnAuthenticationCustomHandler;
import org.oplearn.project.security.error.UnAuthorizationCustomHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

import static org.oplearn.project.constanst.OpLearnConstants.AuthConstant.MATCHER_USER_API;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UnAuthenticationCustomHandler unAuthenticationCustomHandler;
    private final UnAuthorizationCustomHandler unAuthorizationCustomHandler;
    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${cors.allowed-origins}")
    private String defaultAllowedOrigins;

    @Bean
    @Profile("!dev-local")
    public SecurityFilterChain securityFilterChainUsersAPI(HttpSecurity httpSecurity) throws Exception {
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(MATCHER_USER_API).permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unAuthenticationCustomHandler)
                        .accessDeniedHandler(unAuthorizationCustomHandler));
        return httpSecurity.build();
    }

    @Bean
    @Profile("dev-local")
    public SecurityFilterChain securityFilterChainUsersAPILocal(HttpSecurity httpSecurity) throws Exception {
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(MATCHER_USER_API).permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unAuthenticationCustomHandler)
                        .accessDeniedHandler(unAuthorizationCustomHandler));
        return httpSecurity.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChainAdminsAPI(HttpSecurity httpSecurity) throws Exception {
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity
                .securityMatcher(OpLearnConstants.AuthConstant.MATCHER_ADMIN_API)
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().permitAll();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unAuthenticationCustomHandler)
                        .accessDeniedHandler(unAuthorizationCustomHandler));

        return httpSecurity.build();
    }

    private void sharedSecurityConfiguration(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        String activeProfile = profile;
        log.info("====> ActiveProfile: {}", activeProfile);

        if ("prod".equals(activeProfile)) {
            log.info("====> Start corsConfigurationSource, defaultAllowedOrigins: {}", defaultAllowedOrigins);

            List<String> allowedOrigins = Arrays.asList(defaultAllowedOrigins.split("\\s*,\\s*"));
            configuration.setAllowedOrigins(allowedOrigins);
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        } else {
            log.info("====> Start default");
            configuration.addAllowedHeader("*");
            configuration.addAllowedMethod("*");
            configuration.addAllowedOriginPattern("*");
        }

        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
