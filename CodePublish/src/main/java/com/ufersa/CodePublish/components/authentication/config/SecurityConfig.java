package com.ufersa.CodePublish.components.authentication.config;

import com.ufersa.CodePublish.components.authentication.domain.services.CookieFilter;
import com.ufersa.CodePublish.components.authentication.domain.services.JwtTokenFilter;
import com.ufersa.CodePublish.components.authentication.domain.services.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Lazy
    private final TokenService tokenService;
    private final CookieFilter cookieFilter;

    public SecurityConfig(TokenService tokenService, CookieFilter cookieFilter) {
        this.tokenService = tokenService;
        this.cookieFilter = cookieFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(tokenService);

        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(crsf -> crsf.disable())
                //.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(cookieFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("v1/auth/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST,"v1/users")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"v1/publications/**")
                                .permitAll()
                                .anyRequest().authenticated())
                .cors(cors ->{})
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
