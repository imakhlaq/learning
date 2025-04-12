package com.sharefile.securedoc.service;

import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.entity.RoleEntity;
import com.sharefile.securedoc.entity.UserCredentialEntity;
import com.sharefile.securedoc.enumeration.AuthProvider;
import com.sharefile.securedoc.enumeration.LoginType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void createUserForSocialLogin(HttpServletResponse response, String firstName, String lastName, String password,
                                  String email, AuthProvider authProvider, String authProviderId);
    void createUser(String firstName, String lastName, String password,
                    String email);
    RoleEntity getRoleName(String name);
    void verifyAccountKey(String key);
    void updateLoginAttempt(String email, LoginType loginType);
    User getUserByUserId(String userId);
    User getUserByEmail(String email);
    UserCredentialEntity getUserCredentialById(Long id);
    User cancelMfa(Long id);
    User setUpMfa(Long id);
    User verifyQrCode(String userId, String qrCode);
    void resetPassword(String email);
    User verifyPasswordKey(String key);
    void updatePassword(String userId, String newPassword, String confirmNewPassword); //Because the user is not logged in we need to pass in the password
    void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword); // New update password for logged-in user
    User updateUser(String userId, String firstName, String lastName, String email, String phone, String bio);
    void updateRole(String userId, String role);
    void toggleCredentialsExpired(String userId);
    void toggleAccountExpired(String userId);
    void toggleAccountEnabled(String userId);
    void toggleAccountLocked(String userId);
    String uploadPhoto(String userId, MultipartFile file);
    User getUserById(Long id);
}