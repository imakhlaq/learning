package com.sharefile.securedoc.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) //the unknown properties in the body just ignore them
public class LoginUser {
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Invalid email address")
    private String email;
    @NotEmpty(message = "Password can't be empty")
    private String password;
}