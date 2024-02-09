package com.codedailywithme.springSecurityPractice.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private  final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private  static final String[] WHITE_LIST_URL={
            "/api/v1/auth/**",
            "/api-docs/swagger",
            "/api-docs",
            "/api-docs/**",
            "/swagger-ui/index.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"


    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                     req->
                     req.requestMatchers(WHITE_LIST_URL)
                             .permitAll()
                             .anyRequest()
                             .authenticated()

                )
                .sessionManagement(session->
                               session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                .logout(logout->
//                        logout.logoutUrl("/api/v1/auth/logout")
//                                .addLogoutHandler()
//                                .logoutSuccessHandler()
//                )
        return httpSecurity.build();
    }
}
