package com.sharefile.securedoc.validation;

import com.sharefile.securedoc.entity.UserEntity;
import com.sharefile.securedoc.exception.ApiException;

public class UserValidation {
    public static void verifyAccountStatus(UserEntity user) {
        if (!user.getEnabled()) {
            throw new ApiException("User Account is disable, unable to verify ResetPassword cannot be verified");
        }
        if (!user.getAccountNonExpired()) {
            throw new ApiException("User Account is Expired, unable to verify ResetPassword");
        }
        if (!user.getAccountNonLocked()) {
            throw new ApiException("User Account is Locked, unable to verify ResetPassword");
        }
    }
}