package com.example.authserver.config;

import com.example.authserver.config.social.*;
import com.example.authserver.service.RegisteredClientRepositoryImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Configuration
public class SecurityConfig {

    //this is for authorization server
    @Bean
    @Order(1)
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {

        // applyDefaultSecurity method deprecated as of spring security 6.4.2
        var authorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
            .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
            .with(authorizationServerConfigurer, authorizationServer ->
                authorizationServer.oidc(Customizer.withDefaults()) // enable openid connect
            )
            .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());

        // when user try to access an url that without auth
        http
            .exceptionHandling((exceptions) -> // If any errors occur redirect user to login page
                exceptions.defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/login"),
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            )
            // enable auth server to accept JWT for endpoints such as /userinfo
            .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        //configuring the call back url
        //if you want to customize the token endpoint or intosection endpoint you can customize in similar way
        http
            .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .authorizationEndpoint(
                //customizing the callback uri
                a -> a.authenticationProviders(getAuthorizationEndpointProvider())
            );

        http
            // ... other configuration
            .with(authorizationServerConfigurer, authorizationServer ->
                authorizationServer
                    // ... other configuration
                    .clientAuthentication(clientAuthenticationConfigurer ->
                        clientAuthenticationConfigurer
                            .authenticationConverter(new PublicClientRefreshTokenAuthenticationConverter())
                            .authenticationProvider(
                                new PublicClientRefreshTokenAuthenticationProvider(
                                    new RegisteredClientRepositoryImpl().registeredClientRepository(),
                                    new InMemoryOAuth2AuthorizationService() // replace with your AuthorizationService implementation if you have one
                                )
                            )
                    )
            );

        return http.build();
    }

    //this one is for the app
    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .formLogin(formLogin -> formLogin.loginPage("/login").permitAll()) // Enable form login
            .oauth2Login(oauth2Login -> oauth2Login

                .userInfoEndpoint(userInfo -> userInfo
                    .userService(new CustomOAuth2UserService()) //after oauth login customize the object
                )
                .successHandler(new OAuthAuthenticationSuccessHandler(this.userRepo))
                .failureHandler(new OauthAuthenticationFailureHandler())

                .loginPage("/login").permitAll()) // Enable oauth2 federated identity login
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/webjars/**", "/images/**", "/css/**",
                        "/assets/**", "/favicon.ico", "/h2-console/**", "/swagger-ui/**", "/v3/**", "/oauth2/**")
                    .permitAll()
                    .anyRequest().authenticated()
            );

        http
            .exceptionHandling(exception ->
                exception
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint(this.logInURl))
                    .accessDeniedHandler(new CustomAccessDeniedHandler(this.logInURl))
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //using this you can change the default auth server endpoint
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /*
     Authorization server use private key to sign the token,
     And the resource servers uses the public key to verify the token
     */
    //keys to sign the JWT
    //use rotation technique (store the public and private key in db)
    @Bean
    public JWKSource<SecurityContext> jwkSet() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPair = KeyPairGenerator
            .getInstance("RSA");

        keyPair.initialize(2048);

        KeyPair keyPair2 = keyPair.generateKeyPair();

        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair2.getPrivate();

        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair2.getPublic();

        RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
            .privateKey(rsaPrivateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
        var jwkSet = new JWKSet(rsaKey);

        return new ImmutableJWKSet<>(jwkSet);

    }

    //customize the callback url
    public Consumer<List<AuthenticationProvider>> getAuthorizationEndpointProvider() {
        return providers -> {
            for (var provider : providers) {

                if (provider instanceof OAuth2AuthorizationCodeRequestAuthenticationProvider x) {
                    x.setAuthenticationValidator(new CustomRedirectValidator());
                }
            }
        };
    }

}

/*
Here we are using JWT which is non - opack (contain data inside it)

We can also use opack (which doesn't contain data inside)

TO verify the opack token Authorization server uses intosection endpoint to do it.
And send data related to that opack token
 */