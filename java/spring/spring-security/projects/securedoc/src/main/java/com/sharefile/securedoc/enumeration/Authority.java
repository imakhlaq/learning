package com.sharefile.securedoc.enumeration;

import static com.sharefile.securedoc.constant.Constants.*;

public enum Authority {
    USER(USER_AUTHORITY),
    ADMIN(ADMIN_AUTHORITY),
    SUPER_ADMIN(SUPER_ADMIN_AUTHORITY),
    MANAGER(MANAGER_AUTHORITY);

    private String value;

    Authority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}