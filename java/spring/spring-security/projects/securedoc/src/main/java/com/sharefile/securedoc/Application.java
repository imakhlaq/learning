package com.sharefile.securedoc;

import com.sharefile.securedoc.domain.RequestContext;
import com.sharefile.securedoc.entity.RoleEntity;
import com.sharefile.securedoc.enumeration.Authority;
import com.sharefile.securedoc.repository.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, RoleRepo roleRepo) {
        return args -> {

            RequestContext.setUserId(0L);
            var userRoles = new RoleEntity();
            userRoles.setName(Authority.USER.name());
            userRoles.setAuthorities(Authority.USER);
            roleRepo.save(userRoles);

            var adminRoles = new RoleEntity();
            userRoles.setName(Authority.ADMIN.name());
            userRoles.setAuthorities(Authority.ADMIN);
            roleRepo.save(adminRoles);
            RequestContext.onStart();
        };
    }
}