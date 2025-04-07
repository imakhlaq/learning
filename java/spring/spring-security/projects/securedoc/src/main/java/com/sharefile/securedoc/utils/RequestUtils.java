package com.sharefile.securedoc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharefile.securedoc.domain.Response;
import com.sharefile.securedoc.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.time.LocalTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RequestUtils {

    public static Response getResponse(HttpServletRequest request, Map<?, ?> data, String message, HttpStatus status) {

        return new Response(now().toString(),
            status.value(),
            request.getRequestURI(),
            HttpStatus.valueOf(status.value()),
            message,
            EMPTY,
            data
        );
    }

    private static final BiConsumer<HttpServletResponse, Response> writeResponse = (httpServletResponse, response) -> {
        try {
            var outputStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream, response);
            outputStream.flush();
        } catch (Exception e) {
            throw new ApiException("Some exception accused");
        }
    };

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        if (exception instanceof AccessDeniedException e) {
            var apiResponse = getErrorResponse(request, response, exception, FORBIDDEN);
            writeResponse.accept(response, apiResponse);
        }
    }

    private static final BiFunction<Exception, HttpStatus, String> errorReason = (exception, httpStatus) -> {
        if (httpStatus.isSameCodeAs(FORBIDDEN)) {
            return "You are not allowed to access this resource";
        }

        if (httpStatus.isSameCodeAs(UNAUTHORIZED)) {
            return "You are not logged in";
        }
        if (exception instanceof ApiException || exception instanceof DisabledException || exception instanceof AccessDeniedException ||
            exception instanceof LockedException || exception instanceof BadCredentialsException || exception instanceof CredentialsExpiredException) {
            return exception.getMessage();
        }
        if (httpStatus.is5xxServerError()) {
            return "Internal Server Error";
        } else {
            return "An unexpected error occurred";
        }

    };

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                             Exception exception, HttpStatus httpStatus) {
        response.setContentType("application/json");
        response.setStatus(httpStatus.value());
        return new Response(
            LocalDateTime.now().toString(),
            httpStatus.value(),
            request.getRequestURI(),
            HttpStatus.valueOf(httpStatus.value()),
            errorReason.apply(exception, httpStatus),
            getRootCauseMessage(exception),
            emptyMap()
        );
    }
}