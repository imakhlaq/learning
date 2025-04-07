package com.sharefile.securedoc.service.email;

import jakarta.validation.constraints.NotNull;

public interface IEmailService {
    void sendNewAccountEmail(String name, String to, String token);

    void sendPasswordResetEmail(String name, String to, String token);
}