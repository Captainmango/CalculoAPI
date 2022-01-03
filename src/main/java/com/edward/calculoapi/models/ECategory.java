package com.edward.calculoapi.models;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum ECategory {
    CATEGORY_PERSONAL("PERSONAL"),
    CATEGORY_BUSINESS("BUSINESS"),
    CATEGORY_LEISURE("LEISURE"),
    CATEGORY_TRAVEL("TRAVEL"),
    CATEGORY_REQUIRED("REQUIRED"),
    CATEGORY_ONE_OFF("ONE_OFF"),
    CATEGORY_OTHER("OTHER");

    String name;

    ECategory(String name)
    {
        this.name = name;
    }

    public static Boolean exists(String name) {
        if(StringUtils.isBlank(name)) {
            return false;
        }

        for(ECategory categoryType : ECategory.values()) {
            if(name.equals(categoryType.name)) {
                return true;
            }
        }
        return false;
    }

    public static ECategory getByName(String name)
    {
        return Arrays.stream(ECategory.values()).filter(
                eCategory -> eCategory.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}
