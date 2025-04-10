package com.sharefile.securedoc.event;

import com.sharefile.securedoc.entity.UserEntity;
import com.sharefile.securedoc.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType eventType;
    private Map<?, ?> data;//when ever we fire an event we can optionally map in some data. if we don't have
    //any dta we don't have to pass in anything. But anytime we fire a new user event we have to give
    //the user the type and any data associated with that event.
}