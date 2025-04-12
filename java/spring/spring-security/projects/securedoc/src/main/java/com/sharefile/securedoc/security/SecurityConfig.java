package com.sharefile.securedoc.security;

import com.sharefile.securedoc.constant.Constants;
import com.sharefile.securedoc.handlers.ApiAccessDeniedHandler;
import com.sharefile.securedoc.handlers.ApiAuthenticationEntryPoint;
import com.sharefile.securedoc.security.oauth.CustomStatelessAuthorizationRequestRepository;
import com.sharefile.securedoc.security.oauth.OAuth2AuthenticationFailureHandler;
import com.sharefile.securedoc.security.oauth.OAuth2AuthenticationSuccessHandler;
import com.sharefile.securedoc.service.UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.sharefile.securedoc.constant.Constants.BASE_PATH;
import static com.google.common.net.HttpHeaders.X_REQUESTED_WITH;
import static com.sharefile.securedoc.constant.Constants.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.OPTIONS;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {

    private final ApiAccessDeniedHandler apiAccessDeniedHandler;
    private final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;
    private final ApiHttpConfigurer apiHttpConfigurer;

    //oauth
    private final CustomStatelessAuthorizationRequestRepository authorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception ->
                exception.accessDeniedHandler(apiAccessDeniedHandler)
                    .authenticationEntryPoint(apiAuthenticationEntryPoint))
            .authorizeHttpRequests(req ->
                req
                    .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()//for streamingResponseBody
                    .requestMatchers(PUBLIC_URLS).permitAll()//For every http request that matches a specific pattern permit them.
                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/user/delete/**")
                    .hasAnyAuthority("user:delete")
                    .requestMatchers(HttpMethod.DELETE, "/document/delete/**")
                    .hasAnyAuthority("document:delete")
                    .anyRequest().authenticated())//Any other user that does match "Authenticate them"
            .with(apiHttpConfigurer, Customizer.withDefaults());

        //oauth
        http
            .oauth2Login(config ->
                config
                    .authorizationEndpoint(subConfig ->
                        subConfig
                            .baseUri("/oauth2/authorize")
                            //   .authorizationRequestResolver(null)
                            .authorizationRequestRepository(authorizationRequestRepository)
                    )
                    .userInfoEndpoint(userInfoEndpoint ->
                        userInfoEndpoint.userService(null))
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
            );
        return http.build();
    }

    //registering the authentication provider with authentication manager
    //ProviderManager is an implementation of authentication manager
    //instead of user details service we are using our own UserService to fetch user from db etc.
    @Bean
    public ProviderManager providerManager(UserService userDetailsService, PasswordEncoder passwordEncoder) {
        var apiProvider = new ApiAuthenticationProvider(userDetailsService, passwordEncoder);
        return new ProviderManager(apiProvider);
    }

    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://securedoc.com", "http://localhost:4200", "http://localhost:3000", "http://localhost:5173"));
        corsConfiguration.setAllowedHeaders(Arrays.asList(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION,
            X_REQUESTED_WITH, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS, ACCESS_CONTROL_ALLOW_CREDENTIALS, FILE_NAME));
        corsConfiguration.setExposedHeaders(Arrays.asList(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION,
            X_REQUESTED_WITH, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS, ACCESS_CONTROL_ALLOW_CREDENTIALS, FILE_NAME));
        corsConfiguration.setAllowedMethods(Arrays.asList(GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), OPTIONS.name()));
        corsConfiguration.setMaxAge(3600L);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(BASE_PATH, corsConfiguration);
        return source;
    }
}