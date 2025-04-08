package com.sharefile.securedoc.service.JwtService;

import com.sharefile.securedoc.domain.Token;
import com.sharefile.securedoc.domain.TokenData;
import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.enumeration.TokenType;
import com.sharefile.securedoc.function.TriConsumer;
import com.sharefile.securedoc.security.JwtConfiguration;
import com.sharefile.securedoc.service.user.UserServiceImpl;
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

    private final Function<String, Claims> claimsFunction = token -> Jwts.parser()
        .verifyWith(key.get())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    private final Function<String, String> subject = token -> getClaimsValue(token, Claims::getSubject);
    private <T> T getClaimsValue(String token, Function<Claims, T> claims) {
        return claimsFunction.andThen(claims).apply(token);
    }

    private final BiFunction<HttpServletRequest, String, Optional<String>> extractToken = (request, cookieName) ->
        Optional.of(
                Stream.of(request.getCookies() == null ? new Cookie[]{new Cookie(EMPTY_VALUE, EMPTY_VALUE)} : request.getCookies())
                    .filter(cookie -> Objects.equals(cookieName, cookie.getName()))
                    .map(Cookie::getValue)
                    .findAny())
            .orElse(Optional.empty());

    private final BiFunction<HttpServletRequest, String, Optional<Cookie>> extractCookie = (request, cookieName) ->
        Optional.of(
                Stream.of(request.getCookies() == null ? new Cookie[]{new Cookie(EMPTY_VALUE, EMPTY_VALUE)} : request.getCookies())
                    .filter(cookie -> Objects.equals(cookieName, cookie.getName()))
                    .findAny())
            .orElse(Optional.empty());

    private final Supplier<JwtBuilder> jwtBuilder = () -> Jwts
        .builder()
        .header()
        .add(Map.of(TYPE, JWT_TYPE))
        .and()
        .audience()
        .add(GET_ARRAYS_LLC)
        .and()
        .id(UUID.randomUUID().toString())
        .issuedAt(Date.from(Instant.now()))
        .notBefore(new Date())
        .signWith(key.get(), Jwts.SIG.HS512);

    private final BiFunction<User, TokenType, String> buildToken = (user, tokenType) ->
        Objects.equals(tokenType, ACCESS) ? jwtBuilder
            .get()
            .subject(user.getUserId())//setting subject to be userId ( id that will be exposed to the user. it's not PK)
            .claim(AUTHORITIES, user.getAuthorities())
            .claim(ROLE, user.getRole())
            .expiration(Date.from(Instant.now().plusSeconds(getExpiration())))
            .compact() : jwtBuilder
            .get()
            .subject(user.getUserId())
            .expiration(Date.from(Instant.now().plusSeconds(getExpiration())))
            .compact();

    public Function<String, List<GrantedAuthority>> authorities = token -> commaSeparatedStringToAuthorityList(
        new StringJoiner(AUTHORITY_DELIMITER)
            .add(claimsFunction.apply(token).get(AUTHORITIES, String.class))
            .add(ROLE_PREFIX + claimsFunction.apply(token).get(ROLE, String.class))
            .toString()
    );

    private final TriConsumer<HttpServletResponse, User, TokenType> addCookie = (response, user, tokenType) -> {
        switch (tokenType) {
            case ACCESS -> {
                var accessToken = createToken(user, Token::getAccessToken);
                var cookie = new Cookie(tokenType.name(), accessToken);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                //cookie.setSecure(true); only works with https
                cookie.setMaxAge(2 * 60);
                cookie.setAttribute("SameSite", NONE.name());//important because CSRF token will be sent form frontend to backend
                response.addCookie(cookie);
            }
            case REFRESH -> {
                var refreshToken = createToken(user, Token::getRefreshToken);
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

    @Override
    public String createToken(User user, Function<Token, String> tokenFunction) {

        var token = Token.builder()
            .accessToken(buildToken.apply(user, ACCESS))
            .refreshToken(buildToken.apply(user, REFRESH))
            .build();

        return tokenFunction.apply(token);
    }
    @Override
    public Optional<String> extractToken(HttpServletRequest request, String cookieName) {
        return extractToken(request, cookieName);
    }
    @Override
    public void addCookie(HttpServletResponse response, User user, TokenType type) {
        addCookie.accept(response, user, type);
    }
    @Override
    public <T> T getToken(String token, Function<TokenData, T> tokenFunction) {
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
        var optionalCookie = extractCookie.apply(request, cookieName);
        if (optionalCookie.isPresent()) {
            var cookie = optionalCookie.get();
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}