package com.sharefile.securedoc.domain;

public class RequestContext {

    private static final ThreadLocal<Long> userId = new ThreadLocal<>();

    private RequestContext() {
    }

    public static void setUserId(Long userId) {
        setUserId(userId);
    }

    public static void onStart() {
        userId.remove();
    }

    public static Long getUserId() {
        return userId.get();
    }
}