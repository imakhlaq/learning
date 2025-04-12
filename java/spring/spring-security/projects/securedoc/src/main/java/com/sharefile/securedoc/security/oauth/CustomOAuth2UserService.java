package com.sharefile.securedoc.security.oauth;

import com.sharefile.securedoc.domain.UserPrincipal;
import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.entity.UserEntity;
import com.sharefile.securedoc.enumeration.AuthProvider;
import com.sharefile.securedoc.exception.ApiException;
import com.sharefile.securedoc.exception.OAuth2AuthenticationProcessingException;
import com.sharefile.securedoc.security.oauth.user.OAuth2UserInfo;
import com.sharefile.securedoc.security.oauth.user.OAuth2UserInfoFactory;
import com.sharefile.securedoc.repository.UserRepo;
import com.sharefile.securedoc.service.UserService;
import com.sharefile.securedoc.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
The CustomOAuth2UserService extends Spring Security’s DefaultOAuth2UserService and implements its loadUser() method.
 This method is called after an access token is obtained from the OAuth2 provider.

In this method, we first fetch the user’s details from the OAuth2 provider.
If a user with the same email already exists in our database then we update his details, otherwise, we register a new user



Use DefaultOAuth2UserService to get the user details, and use OAuth2AuthorizedClientService in conjunction with that data
 to persist the user information and their OAuth credentials (like tokens) into your database.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepo userRepo;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        var oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    /*
    Below methods checks and register the user
     */

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        //getting what login method is used
        var oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest
            .getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<UserEntity> userEntity = userRepo.findByEmailIgnoreCase(oAuth2UserInfo.getEmail());
        User user;

        //if user is already logged in and the AuthProvider is same then update name and Image
        if (userEntity.isPresent()) {
            var userFromDb = userEntity.get();
            //user email exits, but he is trying to log in with different provider
            if (!userFromDb.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                    userFromDb.getProvider() + " account. Please use your " + userFromDb.getProvider() +
                    " account to login.");
            }
            user = updateExistingUser(userFromDb, oAuth2UserInfo);
        } else {
            //if user is not registered, register him
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        //generating principle to use throughout the application
        var userCredential = userService.getUserCredentialById(user.getId());
        var principal = new UserPrincipal(user, userCredential);
        principal.getUser().setAttributes(oAuth2User.getAttributes());
        return principal;
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {

        var fullName = oAuth2UserInfo.getName().split(" ");
        //create new user
        // and return a User from UserEntity
        return userService.createUserForSocialLogin(
            fullName[0], fullName[1], oAuth2UserInfo.getEmail(),
            AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()),
            oAuth2UserInfo.getId(),
            oAuth2UserInfo.getImageUrl()
        );
    }

    private User updateExistingUser(UserEntity existingUser, OAuth2UserInfo oAuth2UserInfo) {

        var fullName = oAuth2UserInfo.getName().split(" ");
        existingUser.setFirstName(fullName[0]);
        existingUser.setLastName(fullName[1]);
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        userRepo.save(existingUser);
        var credentials = userService.getUserCredentialById(existingUser.getId());
        return UserUtils.fromUserEntity(existingUser, existingUser.getRole(), credentials);
    }

}