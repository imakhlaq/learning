package com.sharefile.securedoc.service.impl;

import com.sharefile.securedoc.domain.Token;
import com.sharefile.securedoc.domain.TokenData;
import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.enumeration.TokenType;
import com.sharefile.securedoc.function.TriConsumer;
import com.sharefile.securedoc.security.JwtConfiguration;
import com.sharefile.securedoc.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.sharefile.securedoc.constant.Constants.*;
import static com.sharefile.securedoc.enumeration.TokenType.ACCESS;
import static com.sharefile.securedoc.enumeration.TokenType.REFRESH;
import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.Header.TYPE;
import static org.springframework.boot.web.server.Cookie.SameSite.NONE;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl extends JwtConfiguration implements JwtService {

    private final UserServiceImpl userService;

    //accessing the key from JwtConfiguration and base64 encoding itt
    private final Supplier<SecretKey> key = () -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(getSecret()));

    /**
     * This function parses a token to extract the claims using a specified key.
     *
     * @param token - The token to parse and extract claims from.
     * @return The Claims extracted from the token.
     */
    private final Function<String, Claims> claimsFunction = token ->
        // Parse the token using the JWT parser.
        Jwts.parser()
            // Verify the token with a specified key.
            .verifyWith(key.get())
            .build()
            // Parse the signed claims from the token.
            .parseSignedClaims(token)
            .getPayload();

    /**
     * This function extracts a token from a request (cookie).
     *
     * @param request - The HttpServletRequest object from which to extract the token.
     * @param cookieName - The name of the cookie to extract the token from.
     * @return An Optional containing the token as a String, or an empty Optional if the token is not found.
     */
    private final BiFunction<HttpServletRequest, String, Optional<String>> extractToken = (request, cookieName) ->
        // Get the cookies from the request, or an empty array if there are no cookies.
        Optional.of(
                Stream.of(
                        request.getCookies() == null ?
                            new Cookie[]{new Cookie(EMPTY_VALUE, EMPTY_VALUE)}
                            : request.getCookies())
                    // Filter the cookies to only include the one with the specified name.
                    .filter(cookie -> Objects.equals(cookieName, cookie.getName()))
                    // Map each cookie to its value.
                    .map(Cookie::getValue)
                    // Find the first cookie value.
                    .findAny())
            // If no token is found, return an empty Optional.
            .orElse(Optional.empty());

    /**
     * This Supplier returns a JwtBuilder object.
     *
     * @return The JwtBuilder object.
     */
    private final Supplier<JwtBuilder> jwtBuilder = () ->
        // Create a new JwtBuilder
        Jwts.builder()
            // Set the header of the JWT to contain a single entry with the key "type" and the value "JWT".
            .header().add(Map.of(TYPE, JWT_TYPE))
            .and()
            .audience().add("GET_ARRAYS_LLC")
            .and()
            // Add the id (unique identifier) of the JWT.
            .id(UUID.randomUUID().toString())
            .issuedAt(Date.from(Instant.now()))
            .notBefore(new Date())
            // Sign the JWT with a specified key and signature algorithm.
            .signWith(key.get(), Jwts.SIG.HS512);

    /**
     * Builds a token based on the provided user and token type.
     *
     * @param {User} user - The user for whom the token is being built.
     * @param {TokenType} type - The type of token being built.
     * @return {string} The built token.
     */
    private final BiFunction<User, TokenType, String> buildToken = (user, tokenType) ->
        // Check if the toke you want to build is access or refresh
        Objects.equals(tokenType, ACCESS) ?
            //in case of access token
            jwtBuilder.get()
                .subject(user.getUserId())//setting subject to be userId ( id that will be exposed to the user. it's not PK)
                .claim(AUTHORITIES, user.getAuthorities())
                .claim(ROLE, user.getRole())
                .expiration(Date.from(Instant.now().plusSeconds(getExpiration())))
                .compact()
            // in case of refresh token
            : jwtBuilder.get()
            .subject(user.getUserId())
            .expiration(Date.from(Instant.now().plusSeconds(getExpiration())))
            .compact();

    /**
     * A function that adds a cookie to the response.
     */
    private final TriConsumer<HttpServletResponse, User, TokenType> addCookie = (response, user, tokenType) -> {
        switch (tokenType) {
            case ACCESS -> {
                // Create an access token for the user.
                var accessToken = createToken(user, Token::getAccess);
                // Create a new cookie.
                var cookie = new Cookie(tokenType.name(), accessToken);
                // Set the cookie properties.
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                //cookie.setSecure(true); only works with https
                cookie.setMaxAge(2 * 60);
                cookie.setAttribute("SameSite", NONE.name());//important because CSRF token will be sent form frontend to backend
                // Add the cookie to the response.
                response.addCookie(cookie);
            }
            case REFRESH -> {
                // Create an access token for the user.
                var refreshToken = createToken(user, Token::getRefresh);
                var cookie = new Cookie(tokenType.name(), refreshToken);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                //cookie.setSecure(true); only works with https
                cookie.setMaxAge(2 * 60 * 60);
                cookie.setAttribute("SameSite", NONE.name());//important because CSRF token will be sent form frontend to backend
                response.addCookie(cookie);
            }
        }
    };

    /**
     * Extracts a specific value from the claims using the provided function.
     *
     * @param <T>    The type of the value to extract.
     * @param token  The token containing the claims.
     * @param claims The function to extract the value from the claims.
     * @return The extracted value.
     */
    private <T> T getClaimsValue(String token, Function<Claims, T> claims) {
        // Apply the claimsFunction to extract the Claims object.
        return claimsFunction.andThen(claims).apply(token);
    }
    private final Function<String, String> subject = token -> getClaimsValue(token, Claims::getSubject);

    private final BiFunction<HttpServletRequest, String, Optional<Cookie>> extractCookie = (request, cookieName) ->
        Optional.of(
                Stream.of(request.getCookies() == null ? new Cookie[]{new Cookie(EMPTY_VALUE, EMPTY_VALUE)} : request.getCookies())
                    .filter(cookie -> Objects.equals(cookieName, cookie.getName()))
                    .findAny())
            .orElse(Optional.empty());

    public Function<String, List<GrantedAuthority>> authorities = token -> commaSeparatedStringToAuthorityList(
        new StringJoiner(AUTHORITY_DELIMITER)
            .add(claimsFunction.apply(token).get(AUTHORITIES, String.class))
            .add(ROLE_PREFIX + claimsFunction.apply(token).get(ROLE, String.class))
            .toString()
    );

    @Override
    public String createToken(User user, Function<Token, String> tokenFunction) {

        var token = Token.builder()
            .access(buildToken.apply(user, ACCESS))
            .refresh(buildToken.apply(user, REFRESH))
            .build();

        return tokenFunction.apply(token);
    }

    //extract the token from the request
    @Override
    public Optional<String> extractToken(HttpServletRequest request, String cookieName) {
        return extractToken(request, cookieName);
    }

    //add access or refresh cookie to the response (base on the TokenType)
    @Override
    public void addCookie(HttpServletResponse response, User user, TokenType type) {
        addCookie.accept(response, user, type);
    }
    @Override
    public <T> T getTokenData(String token, Function<TokenData, T> tokenFunction) {
        return tokenFunction.apply(
            TokenData.builder()
                //comparing the user from db and userId from Jwt
                .isValid(Objects.equals(userService.getUserByUserId(subject.apply(token)).getUserId(), claimsFunction.apply(token).getSubject()))
                .authorities(authorities.apply(token))
                .claims(claimsFunction.apply(token))
                .user(userService.getUserByUserId(subject.apply(token)))//above we set the subject in JWT to be userId
                .build()
        );
    }
    /*
    Removing the cookie from browser/ logging out user
     */
    @Override
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        //extracting cookie from request
        var optionalCookie = extractCookie.apply(request, cookieName);
        if (optionalCookie.isPresent()) {
            //Get the cookie
            var cookie = optionalCookie.get();
            // Expire the cookie by setting the max age to 0
            cookie.setMaxAge(0);
            // Return the cookie to the response
            response.addCookie(cookie);
        }
    }
}