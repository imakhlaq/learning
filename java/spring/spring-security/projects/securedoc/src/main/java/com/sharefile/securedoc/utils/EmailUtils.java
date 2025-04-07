package com.sharefile.securedoc.utils;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token) {

        return String.format("""
            Hello %s! \n\n Your new account has been created.
            Please click the link below to verify your account.\n\n
            %s
            """, name, getVerificationUrl(host, token));
    }

    private static String getVerificationUrl(String host, String token) {
        return host + "/verify/account?token=" + token;
    }

    public static String getPasswordResetMessage(String name, String host, String token) {

        return String.format("""
            Hello %s! \n\n We have received your password reset email.
            Please click the link below to reset the password.\n\n
            %s
            """, name, getResetPasswordUrl(host, token));
    }

    private static String getResetPasswordUrl(String host, String token) {

        return host + "/verify/password?token=" + token;
    }
}