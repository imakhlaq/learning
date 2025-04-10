package com.sharefile.securedoc.enumeration;

public enum TokenType {
    ACCESS("access-token"), REFRESH("refresh-token");
    private final String value; //What ever is defined inside the enum "" is what i call value.

    //Define Constructor
    TokenType(String value) {
        this.value = value;
    }
    //Define getter for the value
    public String getValue() {
        return value;
    }
}