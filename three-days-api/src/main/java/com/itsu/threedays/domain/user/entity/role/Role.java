package com.itsu.threedays.domain.user.entity.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("USER"),
    ADMIN("ADMIN"),
    SOCIAL("SOCIAL");
    private final String value;

}
