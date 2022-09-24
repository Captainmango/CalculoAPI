package com.edward.calculoapi.api.models;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum ERole {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    final String name;

    ERole(String name)
    {
        this.name = name;
    }

    public static Boolean exists(String name)
    {
        if(StringUtils.isBlank(name)) {
            return false;
        }

        return Arrays.stream(ERole.values())
                .anyMatch(role -> role.name.equals(name.toUpperCase()));
    }

    public static ERole getByName(String name)
    {
        return Arrays.stream(ERole.values())
                .filter(
                        eRole -> eRole.name.equals(name.toUpperCase())
                )
                .findFirst()
                .orElse(null);
    }
}
