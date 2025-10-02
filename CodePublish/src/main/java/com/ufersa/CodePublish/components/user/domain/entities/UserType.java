package com.ufersa.CodePublish.components.user.domain.entities;

public enum UserType {
    ADMIN("ADMIN"),
    STANDARD("STANDARD");

    private String value;

    private UserType(String value) {
        this.value = value;
    }
}
