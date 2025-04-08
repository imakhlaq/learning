package com.sharefile.securedoc.security;

import com.sharefile.securedoc.service.user.IUserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {

    private final LoginAuthenticationFilter loginAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .cors(cors ->
                cors.configurationSource(request -> {
                        var corsConfig = new CorsConfiguration();
                        corsConfig.setAllowedOrigins(listOf("http://localhost:3000", "http://127.0.0.1:3000"));
                        corsConfig.setAllowedMethods(listOf("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        corsConfig.setAllowedHeaders(listOf("*"));
                        return corsConfig;
                    }
                )
            )
            .authorizeHttpRequests(req ->
                req
                    .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()//for streamingResponseBody
                    .requestMatchers("/user/register", "/user/verify/account").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

/*    @Bean
    public UserDetailsService userDetailsService() {

        var user1 = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("ADMIN")
            .build();
        var user2 = User.withDefaultPasswordEncoder()
            .username("user")
            .password("user")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }*/

    //registering the authentication provider with authentication manager
    //ProviderManager is an implementation of authentication manager
    @Bean
    public ProviderManager providerManager(IUserService userDetailsService, PasswordEncoder passwordEncoder) {
        var apiProvider = new ApiAuthenticationProvider(userDetailsService, passwordEncoder);
        return new ProviderManager(apiProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}