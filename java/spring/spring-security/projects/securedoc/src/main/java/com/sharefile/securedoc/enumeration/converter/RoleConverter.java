package com.sharefile.securedoc.enumeration.converter;

import com.sharefile.securedoc.enumeration.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority, String> {

    //when saving to the db
    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if (authority == null) return null;

        return authority.getValue();
    }

    //when retrive from the db
    @Override
    public Authority convertToEntityAttribute(String s) {
        if (s == null) return null;

        return Stream.of(Authority.values())
            .filter(authority -> authority.getValue().equals(s))
            .findFirst()
            .orElseThrow(IllegalAccessError::new);
    }
}