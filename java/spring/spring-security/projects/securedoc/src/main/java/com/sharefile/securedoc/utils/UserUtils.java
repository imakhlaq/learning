package com.sharefile.securedoc.utils;

import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.entity.RoleEntity;
import com.sharefile.securedoc.entity.UserCredentialEntity;
import com.sharefile.securedoc.entity.UserEntity;
import com.sharefile.securedoc.exception.ApiException;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.sharefile.securedoc.constant.Constants.THE_IN3ROVERT_LLC;
import static dev.samstevens.totp.util.Utils.getDataUriForImage;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UserUtils {

    public static UserEntity createUserEntity(String firstName, String lastName,
                                              String email, RoleEntity role) {
        return UserEntity.builder()
            .userId(UUID.randomUUID().toString())
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .role(role)
            .lastLogin(LocalDateTime.now())
            .loginAttempts(0)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .accountNonLocked(true)
            .enabled(false)
            .bio(EMPTY)
            .phone(EMPTY)
            .qrCodeSecret(EMPTY)
            .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
            .build();
    }

    public static User fromUserEntity(UserEntity userEntity, RoleEntity role,
                                      UserCredentialEntity userCredentialEntity) {
        var user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialsNonExpired(isCredentialExpired(userCredentialEntity));
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setRole(role.getName());
        user.setAuthorities(role.getAuthorities().getValue());
        return user;
    }
    public static Boolean isCredentialExpired(UserCredentialEntity userCredentialEntity) {
        return userCredentialEntity.getCreatedAt().plusDays(90).isBefore(LocalDateTime.now());
    }

    //2 Helper method to create the QR code data
    public static BiFunction<String, String, QrData> qrDataFunction = (email, qrCodeSecret) -> new QrData.Builder()
        .issuer(THE_IN3ROVERT_LLC)
        .label(email)
        .secret(qrCodeSecret)
        .algorithm(HashingAlgorithm.SHA1)
        .digits(6)
        .period(30) //Refreshes every thirty seconds QR refreshes
        .build();

    // 1 Create the qrcode uri
    public static BiFunction<String, String, String> qrCodeImageUri = (email, qrCodeSecret) -> {
        //Takes in a QRCode data
        var data = qrDataFunction.apply(email, qrCodeSecret);
        //Then generate the QrCode Image
        var generator = new ZxingPngQrGenerator();
        byte[] imageData;
        try {
            imageData = generator.generate(data);
        } catch (Exception exception) {
            throw new ApiException("Unable to create QR code Uri");
        }
        return getDataUriForImage(imageData, generator.getImageMimeType());
    };

    //3 create the qr code secret code used for QRCode Generation
    public static Supplier<String> qrCodeSecret = () -> new DefaultSecretGenerator().generate(); // This will generate the secret
}