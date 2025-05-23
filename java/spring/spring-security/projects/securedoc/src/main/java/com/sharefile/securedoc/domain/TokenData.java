package com.sharefile.securedoc.domain;

import com.sharefile.securedoc.dto.User;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@Setter
public class TokenData {
    private User user;
    private Claims claims;//Coming from the jjwt lib
    private boolean isValid;
    private List<GrantedAuthority> authorities; //AUTHORITIES ASSOCIATED WITH THE TOKEN.

}