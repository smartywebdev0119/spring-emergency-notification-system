package com.example.recipient.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Url {
    RECIPIENTS("/api/v1/recipients/"),
    FILES("/api/v1/files/"),
    REGISTER("/api/v1/auth/register"),
    AUTHENTICATE("/api/v1/auth/authenticate"),
    LOGOUT("/api/v1/auth/logout");

    private final String constant;

    @Override
    public String toString() {
        return constant;
    }
}
