package com.akhlaq.securenote;

import com.akhlaq.securenote.models.AppRoles;
import com.akhlaq.securenote.models.Role;
import com.akhlaq.securenote.models.User;
import com.akhlaq.securenote.repositories.RoleRepo;
import com.akhlaq.securenote.repositories.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepo userRepo, RoleRepo roleRepo) {
        return args -> {

            Role userRole = roleRepo.findByRoleName(AppRoles.ROLE_USER)
                .orElseGet(() -> roleRepo.save(new Role(AppRoles.ROLE_USER)));

            Role adminRole = roleRepo.findByRoleName(AppRoles.ROLE_ADMIN)
                .orElseGet(() -> roleRepo.save(new Role(AppRoles.ROLE_ADMIN)));

            if (!userRepo.existsByUsername("user1")) {
                var newUser = new User("user1", "exa@gmail.com", "{noop}daifsfjgklsfkanfk");
                newUser.setAccountNonLocked(false);
                newUser.setAccountNonExpired(true);
                newUser.setCredentialsNonExpired(true);
                newUser.setEnabled(true);
                newUser.setAccountExpireDate(LocalDate.now().plusDays(30));
                newUser.setCreateDate(LocalDateTime.now().plusDays(30));
                newUser.setSignUpMethod("gmail");
                newUser.setRole(userRole);
                newUser.setTwoFactorEnabled(false);
                userRepo.save(newUser);
            }
        };
    }

}