package com.sharefile.securedoc.enumeration.converter;

import com.sharefile.securedoc.enumeration.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority, String> {

    /**
     * ===================
     * Converts an Authority object to its corresponding string representation for storage in the database.
     *
     * @param authority the Authority object to be converted
     * @return the string representation of the Authority object, or null if the input is null
     * ====================
     */
    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if (authority == null) return null;

        return authority.getValue();
    }

    /**
     * ====================
     * Converts a string code(value) to its corresponding Authority enum value -> (if(code(value) = value))
     *
     * @param code the string code to be converted
     * @return the Authority enum value corresponding to the code, or null if the input is null
     * @throws IllegalArgumentException if no corresponding Authority enum value is found for the code
     *                                  ====================
     */
    @Override
    public Authority convertToEntityAttribute(String code) {
        if (code == null) return null;

        return Stream.of(Authority.values())
            .filter(authority -> authority.getValue().equals(code))
            .findFirst()
            .orElseThrow(IllegalAccessError::new);
    }
}