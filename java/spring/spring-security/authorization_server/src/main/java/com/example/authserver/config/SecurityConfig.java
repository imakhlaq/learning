package com.example.authserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Configuration
public class SecurityConfig {

    //this is for authorization server
    @Bean
    @Order(1)
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());

        //configuring the call back url
        //if you want to customize the token endpoint or intosection endpoint you can customize in similar way
        http
            .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .authorizationEndpoint(
                //customizing the callback uri
                a -> a.authenticationProviders(getAuthorizationEndpointProvider())
            );

        // when user try to access an url that without auth
        http.exceptionHandling(e -> {
            e.authenticationEntryPoint(
                new LoginUrlAuthenticationEntryPoint("/login")
            );
        });

        return http.build();
    }

    //this one is for the app
    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {

        http
            .formLogin(Customizer.withDefaults())
            .authorizeHttpRequests(authorizeRequests -> {
                authorizeRequests.anyRequest().authenticated();
            });

        return http.build();
    }

    //this represents one user that is registered with the auth server
    @Bean
    public UserDetailsService userDetailsService() {

        var u1 = User
            .withUsername("bill")
            .password(passwordEncoder().encode("123"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(u1);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //this represents one client that is registered with the auth server
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        var r1 = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId("client")
            .clientSecret("secret")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.EMAIL)
            .redirectUri("https://springone.io/authorize")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenFormat(OAuth2TokenFormat.REFERENCE)//means the non opack token
                    .accessTokenTimeToLive(Duration.ofSeconds(9000))

                    .build()
            )
            .build();

        return new InMemoryRegisteredClientRepository(r1);
    }

    //using this you can change the default auth server endpoint
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    //keys to sign the JWT
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

    //to customize the JWT
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return context -> {
            context.getClaims().claim("test", "test");
        };
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