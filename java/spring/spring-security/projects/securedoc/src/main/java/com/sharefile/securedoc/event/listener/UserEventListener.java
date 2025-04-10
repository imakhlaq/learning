package com.sharefile.securedoc.event.listener;

import com.sharefile.securedoc.event.UserEvent;
import com.sharefile.securedoc.service.email.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component //Event listener is needed to be a bean
@RequiredArgsConstructor
public class UserEventListener {

    private final IEmailService emailService;

    @EventListener(UserEvent.class)
    public void onUserEvent(UserEvent event) {
        switch (event.getEventType()) {
            case REGISTRATION -> emailService
                .sendNewAccountEmail(event.getUser().getFirstName(),
                    event.getUser().getEmail(),
                    (String) event.getData().get("key"));

            case RESETPASSWORD -> emailService
                .sendPasswordResetEmail(event.getUser().getFirstName(),
                    event.getUser().getEmail(),
                    (String) event.getData().get("key"));
        }
    }
}