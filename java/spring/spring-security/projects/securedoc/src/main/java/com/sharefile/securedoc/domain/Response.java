package com.sharefile.securedoc.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)//any property with default value ignore it
public record Response(
    String time,
    Integer code,
    String Path,
    HttpStatus status,
    String message,
    String exception,
    Map<?, ?> data
) {
}