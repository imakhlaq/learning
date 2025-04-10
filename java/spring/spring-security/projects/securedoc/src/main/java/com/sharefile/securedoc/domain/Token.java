package com.sharefile.securedoc.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Builder
@Getter
@Setter
public class Token {
    private String access;
    private String refresh;
}