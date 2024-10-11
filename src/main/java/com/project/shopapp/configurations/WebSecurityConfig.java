package com.project.shopapp.configurations;

import com.project.shopapp.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req -> {
                    req
                            .requestMatchers(
                                    String.format( "/api/v1/users/register/**"),
                                    String.format("/api/v1/users/login/**")
                            )
                            .permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("/orders/**")).hasAnyRole("USER")

                            .requestMatchers(HttpMethod.GET,
                                    String.format("/orders/**")).hasAnyRole("USER", "ADMIN")

                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("/orders/**")).hasRole("ADMIN")

                            .requestMatchers(HttpMethod.PUT,
                                    String.format("/orders/**")).hasRole("ADMIN")
                            .anyRequest().authenticated();

                });
    return http.build();
    }
}
