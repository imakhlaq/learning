package com.sharefile.securedoc.service.impl;

import com.sharefile.securedoc.cache.CacheStore;
import com.sharefile.securedoc.domain.RequestContext;
import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.entity.ConfirmationEntity;
import com.sharefile.securedoc.entity.RoleEntity;
import com.sharefile.securedoc.entity.UserCredentialEntity;
import com.sharefile.securedoc.enumeration.Authority;
import com.sharefile.securedoc.enumeration.EventType;
import com.sharefile.securedoc.enumeration.LoginType;
import com.sharefile.securedoc.event.UserEvent;
import com.sharefile.securedoc.exception.ApiException;
import com.sharefile.securedoc.repository.ConfirmationRepo;
import com.sharefile.securedoc.repository.CredentialRepo;
import com.sharefile.securedoc.repository.RoleRepo;
import com.sharefile.securedoc.repository.UserRepo;
import com.sharefile.securedoc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static com.sharefile.securedoc.utils.UserUtils.createUserEntity;
import static com.sharefile.securedoc.utils.UserUtils.fromUserEntity;

@Service
@Transactional(rollbackFor = Exception.class)//rollback on any exception
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final CredentialRepo credentialRepo;
    private final ConfirmationRepo confirmationRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final CacheStore<String, Integer> userCacheStore;

    @Override
    public void createUser(String firstName, String lastName, String password, String email) {

        var role = getRoleName(Authority.USER.name());
        var userEntity = userRepo.save(createUserEntity(firstName, lastName, email, role));
        var credentialEntity = new UserCredentialEntity(userEntity, password);
        credentialRepo.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity();
        confirmationEntity.setUser(userEntity);
        confirmationRepo.save(confirmationEntity);
        eventPublisher.publishEvent(new UserEvent(userEntity,
            EventType.REGISTRATION,
            Map.of("key", confirmationEntity.getKey())));

    }

    @Override
    public User getUserByUserId(String userId) {
        var userEntity = userRepo.findByUserId(userId).orElseThrow(() -> new ApiException("User Not found"));
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }
    @Override
    public User getUserByEmail(String email) {
        var userEntity = userRepo.findAllByEmailIgnoreCase(email).orElseThrow(() -> new ApiException("No credential found"));
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public UserCredentialEntity getUserCredentialById(Long userId) {
        var credentialById = credentialRepo.getReferenceById(userId);
        return credentialById.orElseThrow(() -> new ApiException("No credential found"));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepo.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException("Role not found"));
    }
    @Override
    public void verifyToken(String key) {

        var confirmationEntity = confirmationRepo.findByKey(key);
        if (confirmationEntity.isEmpty()) {
            throw new ApiException("Confirmation not found");
        }
        var userEntity = userRepo.findAllByEmailIgnoreCase(confirmationEntity.get().getUser().getEmail());

        if (userEntity.isEmpty()) {
            throw new ApiException("User not found");
        }

        var user = userEntity.get();
        user.setEnable(true);
        userRepo.save(user);

        var confirmation = confirmationEntity.get();
        confirmation.setKey("");
        confirmationRepo.delete(confirmation);
    }
    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = userRepo.findAllByEmailIgnoreCase(email).orElseThrow(() -> new ApiException("User not found"));
        RequestContext.setUserId(userEntity.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCacheStore.get(userEntity.getFirstName()) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCacheStore.put(userEntity.getEmail(), userEntity.getLoginAttempts());
                if (userCacheStore.get(userEntity.getEmail()) > 5) {
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setLoginAttempts(0);
                userEntity.setAccountNonLocked(false);
                userEntity.setLastLogin(LocalDateTime.now());
                userCacheStore.evict(userEntity.getEmail());
            }
        }
        userRepo.save(userEntity);
    }
}