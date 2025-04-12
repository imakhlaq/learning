package com.sharefile.securedoc.service.impl;

import com.sharefile.securedoc.cache.CacheStore;
import com.sharefile.securedoc.domain.RequestContext;
import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.entity.ConfirmationEntity;
import com.sharefile.securedoc.entity.RoleEntity;
import com.sharefile.securedoc.entity.UserCredentialEntity;
import com.sharefile.securedoc.entity.UserEntity;
import com.sharefile.securedoc.enumeration.AuthProvider;
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
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.BiFunction;

import static com.sharefile.securedoc.constant.Constants.NINETY_DAYS;
import static com.sharefile.securedoc.constant.Constants.PHOTO_DIR;
import static com.sharefile.securedoc.enumeration.EventType.RESETPASSWORD;
import static com.sharefile.securedoc.utils.UserUtils.*;
import static com.sharefile.securedoc.validation.UserValidation.verifyAccountStatus;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.time.LocalDateTime.now;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
@Transactional(rollbackFor = Exception.class)//rollback on any exception
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final CredentialRepo credentialRepo;
    private final ConfirmationRepo confirmationRepo;
    private final ApplicationEventPublisher eventPublisher;//To publish an event when a user is created so that we can get an email.
    private final PasswordEncoder passwordEncoder;
    private final CacheStore<String, Integer> userCacheStore;
    private final UserService userService;

    private ConfirmationEntity getUserConfirmation(UserEntity user) {
        return confirmationRepo.findByUserEntity(user).orElse(null);
    }

    //Creating helper method for the createNewUser method
    private UserEntity createNewUser(String firstName, String lastName, String email) {
        //creating and a user role and saving it to db
        var role = getRoleName(Authority.USER.name()); //Find a role in the database by the name USER
        return createUserEntity(firstName, lastName, email, role);
    }
    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepo.findByKey(key)
            .orElseThrow(() -> new ApiException("Confirmation Key not Found"));
    }
    private UserEntity getUserEntityById(Long id) {
        var userById = userRepo.findById(id);
        return userById.orElseThrow(() -> new ApiException("User not found for MFA uses"));
    }

    private UserEntity getUserEntityByEmail(String email) {
        return userRepo.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new ApiException("User not Found, UserEntity cannot be found by email"));
    }
    private UserEntity getUserEntityByUserId(String userId) {
        return userRepo.findUserByUserId(userId)
            .orElseThrow(() -> new ApiException("User not found"));
    }

    @Override
    public User getUserById(Long id) {
        var userEntity = getUserEntityById(id);
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    //when user signs in using social login
    @Override
    public User createUserForSocialLogin(String firstName, String lastName, String email,
                                         AuthProvider authProvider, String authProviderId, String imageUrl) {

        //creating and a user role and saving it to db
        var userEntity = userRepo.save(createNewUser(firstName, lastName, email));
        userEntity.setProvider(authProvider);
        userEntity.setProviderId(authProviderId);
        userEntity.setImageUrl(imageUrl);
        //creating user credential with encoded password
        var credentialEntity = new UserCredentialEntity(userEntity, null);//no password in auth login
        credentialRepo.save(credentialEntity);
        userRepo.save(userEntity);
        
        return fromUserEntity(userEntity, userEntity.getRole(), credentialEntity);
    }
    @Override
    public void createUser(String firstName, String lastName, String password,
                           String email) {
        //TODO perform checks like user already exits etc before saving

        //creating and a user role and saving it to db
        var userEntity = userRepo.save(createNewUser(firstName, lastName, email));
        userEntity.setProvider(AuthProvider.local);
        userEntity.setProviderId(null);
        //creating user credential with encoded password
        var credentialEntity = new UserCredentialEntity(userEntity, passwordEncoder.encode(password));//encode the password
        credentialRepo.save(credentialEntity);
        //creating a confirmation entity
        //confirmEntity have a UUID that is sent in the url email as link to confirm the user email
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepo.save(confirmationEntity);
        System.out.println(confirmationEntity.getKey());
        //publishing user created event so that it can send email to user to confirm its email
        eventPublisher.publishEvent(new UserEvent(userEntity,
            EventType.REGISTRATION,
            Map.of("key", confirmationEntity.getKey())));
    }

    //get the role base on the role name (ADMIN,USER etc.)
    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepo.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException("Role not found"));
    }

    // This method verify the key send to verify the user email
    @Override
    public void verifyAccountKey(String key) {
        //get the confirmation entity with the key
        var confirmationEntity = getUserConfirmation(key);

        //get the user related to the confirmation
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());

        //enable the user account
        userEntity.setEnabled(true);
        userRepo.save(userEntity);

        //removing the key and deleting the confirmation entity
        confirmationEntity.setKey("");
        confirmationRepo.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        //Set the userId in the request context because we are going to save iin the database we know who did.
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
                userEntity.setLastLogin(now());
                userCacheStore.evict(userEntity.getEmail());
            }
        }
        userRepo.save(userEntity);
    }
    //Retrieve users based on their userId
    @Override
    public User getUserByUserId(String userId) {
        var userEntity = userRepo.findUserByUserId(userId)
            .orElseThrow(() -> new ApiException("User Not found"));
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }
    //Retrieve users based on their email
    @Override
    public User getUserByEmail(String email) {
        var userEntity = getUserEntityByEmail(email);
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }
    //Retrieve users credentials based on their id
    @Override
    public UserCredentialEntity getUserCredentialById(Long userId) {
        var credentialById = credentialRepo.getUserCredentialEntityByUserEntityId(userId);
        return credentialById.orElseThrow(() -> new ApiException("No credential found"));
    }

    @Override
    public User setUpMfa(Long id) {
        var userEntity = getUserEntityById(id);
        var codeSecret = qrCodeSecret.get();
        userEntity.setQrCodeImageUri(qrCodeImageUri.apply(userEntity.getEmail(), codeSecret));
        userEntity.setQrCodeSecret(codeSecret);
        userEntity.setEnabled(true);
        userRepo.save(userEntity);
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User verifyQrCode(String userId, String qrCode) {
        var userEntity = getUserEntityByUserId(userId);
        verifyCode(qrCode, userEntity.getQrCodeSecret());
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }
    //Verify the codes
    private boolean verifyCode(String qrCode, String qrCodeSecret) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        log.debug("Verifying QR Code {}", qrCode);

        if (codeVerifier.isValidCode(qrCodeSecret, qrCode)) {
            return true;
        } else {
            throw new ApiException("Invalid QR code. Please try again");
        }
    }

    @Override
    public User cancelMfa(Long id) {
        //Lets get the user entity
        var userEntity = getUserEntityById(id);
        userEntity.setMfa(false);
        userEntity.setQrCodeImageUri(EMPTY);
        userEntity.setQrCodeSecret(EMPTY);
        userRepo.save(userEntity);
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public void resetPassword(String email) {
        // get the user
        var user = getUserEntityByEmail(email);
        // create a confirmation entity related to the user
        var confirmation = getUserConfirmation(user);
        if (confirmation != null) {
            //Send existing confirmation
            eventPublisher.publishEvent(new UserEvent(user, RESETPASSWORD, Map.of("key", confirmation.getKey())));
        } else {
            // Create new confirmation, save and send email to reset he pass word
            var confirmationEntity = new ConfirmationEntity(user);
            System.out.println("Confirmation Entity for ResetPassword -------------: " + confirmationEntity);
            confirmationRepo.save(confirmationEntity);
            eventPublisher.publishEvent(new UserEvent(user, RESETPASSWORD, Map.of("key", confirmationEntity.getKey())));
        }

    }
    @Override
    public User verifyPasswordKey(String key) {
        // Find the confirmation entity in the database, it confirmation not found
        var confirmationEntity = getUserConfirmation(key);

        // If the confirmation is found
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());

        verifyAccountStatus(userEntity);
        confirmationRepo.delete(confirmationEntity); //Optional but we don't want to many confirmation data for each user.
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }
    //when user apply for forgotten password
    @Override
    public void updatePassword(String userId, String newPassword, String confirmNewPassword) {
        if (!confirmNewPassword.equals(newPassword)) {
            throw new ApiException("Password don't match. Please try again");
        }
        var user = getUserByUserId(userId);
        var credentials = getUserCredentialById(user.getId());
        credentials.setPassword(passwordEncoder.encode(newPassword));
        credentialRepo.save(credentials);
    }

    //when user is logged in the app and try to change the password
    @Override
    public void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!confirmNewPassword.equals(newPassword)) {
            throw new ApiException("New currentPassword don't match");
        }
        var user = getUserEntityByUserId(userId);
        // Verify if the user is allowed to update their account
        verifyAccountStatus(user);
        var credentials = getUserCredentialById(user.getId());
        // Check if the current password from db matches the currentPassword in the request/provided
        if (!passwordEncoder.matches(currentPassword, credentials.getPassword())) {
            throw new ApiException(" Existing currentPassword is incorrect, Kindly try again");
        }
        credentials.setPassword(passwordEncoder.encode(newPassword));
        credentialRepo.save(credentials);
    }
    @Override
    public User updateUser(String userId, String firstName, String lastName, String email, String phone, String bio) {
        var userEntity = getUserEntityByUserId(userId); // UserId is coming from the logged-in user
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setPhone(phone);
        userEntity.setBio(bio);
        userRepo.save(userEntity);
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }
    @Override
    public void updateRole(String userId, String role) {
        //We need the UserEntity
        var userEntity = getUserEntityByUserId(userId);
        //getting the role using the roleName and setting it
        userEntity.setRole(getRoleName(role));
        log.debug("User role to be updated {}", userEntity);
        //Save role name to database
        userRepo.save(userEntity);

    }
    @Override
    public void toggleCredentialsExpired(String userId) {
        var userEntity = getUserEntityByUserId(userId);
        var credentials = getUserCredentialById(userEntity.getId());
        //credentials.setUpdatedAt(LocalDateTime.of(1995, 7, 12, 11, 11));
        // A better approach
        // If the credentials is over 90days
        if (credentials.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(now())) {
            // Make the updated at to now
            credentials.setUpdatedAt(now());
        } else {
            // Make it expire
            credentials.setUpdatedAt(LocalDateTime.of(1995, 7, 12, 11, 11));
        }
        userRepo.save(userEntity);

    }
    @Override
    public void toggleAccountExpired(String userId) {
        var userEntity = getUserEntityByUserId(userId);
        userEntity.setAccountNonExpired(!userEntity.getAccountNonExpired());
        userRepo.save(userEntity);

    }
    @Override
    public void toggleAccountEnabled(String userId) {
        var userEntity = getUserEntityByUserId(userId);
        userEntity.setEnabled(!userEntity.getEnabled());
        userRepo.save(userEntity);
    }
    @Override
    public void toggleAccountLocked(String userId) {
        var userEntity = getUserEntityByUserId(userId);
        userEntity.setAccountNonLocked(!userEntity.getAccountNonLocked());
        userRepo.save(userEntity);
    }
    @Override
    public String uploadPhoto(String userId, MultipartFile file) {
        //Get the user entity
        var user = getUserEntityByUserId(userId);
        //Create helper method
        var photoUrl = photoFunction.apply(userId, file);
        /* Make sure the url is diff everytime its updated so the will not refetch the image
        The reason we want to change the url everytime is because
        we want the browser to fetch the image everytime the sources attribute is different
         */
        user.setImageUrl(photoUrl + "?timestamp=" + System.currentTimeMillis());
        userRepo.save(user);
        return photoUrl;
    }

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, file) -> {
        /*
        Going to use the string UUID which is the userId of the user can also
        use a random generated string or UUID as the image url of the user.
         */
        var filename = id + ".png";
        try {
            var fileStorageLocation = Paths.get(PHOTO_DIR).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(file.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/image/" + filename).toUriString();
        } catch (Exception exception) {
            throw new ApiException("Unable to save image");
        }
    };
}