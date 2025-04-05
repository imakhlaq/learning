package com.example.securitywithbasicauth.config;

import com.example.securitywithbasicauth.config.security.filter.ApiKeyFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@PreAuthorize @PostAuthorize @PreFilter @PostFilter
public class WebSecurityConfig {

    private String apiKey = "dadadadadadadad";
    private final AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //build in Auth provider
        http
            .httpBasic(Customizer.withDefaults());

        //add customAuthFilter in place of UsernamePasswordAuthenticationFilter
//        http
//            .addFilterAt(customAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // before BasicAuthenticationFilter add Api key filter
        http
            .addFilterBefore(new ApiKeyFilter(apiKey, authenticationManager), BasicAuthenticationFilter.class);

        //setting the public and protected routes
        http
            .authorizeHttpRequests(auth -> {

                //for streamingResponseBody
                auth.dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll();

                //these are whitelist paths
                auth.requestMatchers("/", "/hello").permitAll();

                //no one can access this endpoint
                auth.requestMatchers("/idk").denyAll();

                //only authenticated users can access these
                auth.requestMatchers("/api/upload", "/api/myfiles", "/api/download")
                    .authenticated();

                // authenticated user with read authority can access these
                auth.requestMatchers("/api/huad", "/api/dee", "/api/huu")
                    .hasAuthority("read");

                // authenticated user with read or write or piss  authority can access these
                auth.requestMatchers("/api/huad", "/api/maneedadr", "/api/huu")
                    .hasAnyAuthority("read", "write", "piss");

                /// authenticated user with ADMIN role
                //roles("ADMIN") method  will prefix role with ROLE_ADMIN
                //or authorities("ROLE_ADMIN")
                auth.requestMatchers("/api/admin", "/api/huu")
                    .hasRole("ADMIN");

                // authenticated user with ADMIN or MANAGER role
                auth.requestMatchers("/api/manager", "/daeakd/dad")
                    .hasAnyRole("ADMIN", "MANAGER");

                //you can mix and match the hasAnyRole() and hasAuthority)
                auth.requestMatchers("/zerbta")
                    .hasAnyRole("ADMIN", "MANAGER");
                auth.requestMatchers("/zerbta")
                    .hasAnyAuthority("read", "write");

                auth.requestMatchers("/delete")
                    .hasAnyRole("ADMIN", "MANAGER");
                auth.requestMatchers("/delete")
                    .hasAnyAuthority("delete");

                //access() using this you can write SpEL logic and even call beans methods its powerful
//                auth.anyRequest()
//                    .access()

            });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //encoder that doesn't encode the password
        return NoOpPasswordEncoder.getInstance();
    }

    //storing the authentication manager in the context
    //inject this authentication manager where you want to authenticate a user (/login) and provide it a ApiAuthentication object
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}