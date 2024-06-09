package org.dongguk.ownsaemiro.ownsaemiroserver.security.config;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.filter.JwtAuthenticationFilter;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.filter.JwtExceptionFilter;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.handler.exception.CustomAccessDeniedHandler;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.handler.exception.CustomAuthenticationEntryPointHandler;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.handler.login.DefaultFailureHandler;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.handler.login.DefaultSuccessHandler;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.handler.logout.CustomLogoutProcessHandler;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.handler.logout.CustomLogoutResultHandler;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.provider.JwtAuthenticationManager;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final DefaultSuccessHandler defaultSuccessHandler;
    private final DefaultFailureHandler defaultFailureHandler;
    private final CustomLogoutProcessHandler customLogoutProcessHandler;
    private final CustomLogoutResultHandler customLogoutResultHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/login").permitAll()
                                .requestMatchers(Constants.NO_NEED_AUTH.toArray(String[]::new)).permitAll()
                                .requestMatchers(Constants.NEED_USER_ROLE.toArray(String[]::new)).hasRole("USER")
                                .requestMatchers(Constants.NEED_SELLER_ROLE.toArray(String[]::new)).hasRole("SELLER")
                                .requestMatchers(Constants.NEED_ADMIN_ROLE.toArray(String[]::new)).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/api/auth/sign-in")
                        .usernameParameter("serial_id")
                        .passwordParameter("password")
                        .successHandler(defaultSuccessHandler)
                        .failureHandler(defaultFailureHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .addLogoutHandler(customLogoutProcessHandler)
                        .logoutSuccessHandler(customLogoutResultHandler)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPointHandler)
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtUtil, jwtAuthenticationManager), LogoutFilter.class
                )
                .addFilterBefore(
                        new JwtExceptionFilter(), JwtAuthenticationFilter.class
                )
                .getOrBuild();
    }
}
