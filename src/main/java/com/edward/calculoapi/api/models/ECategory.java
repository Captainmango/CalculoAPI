package com.edward.calculoapi.api.models;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum ECategory {
    CATEGORY_GENERAL("GENERAL"),
    CATEGORY_GROCERIES("GROCERIES"),
    CATEGORY_EATING_OUT("EATING OUT"),
    CATEGORY_TRAVEL("TRAVEL"),
    CATEGORY_BILLS("BILLS"),
    CATEGORY_CHARITY("CHARITY"),
    CATEGORY_ENTERTAINMENT("ENTERTAINMENT"),
    CATEGORY_FAMILY("FAMILY"),
    CATEGORY_FINANCES("FINANCES"),
    CATEGORY_GIFTS("GIFTS"),
    CATEGORY_HOLIDAYS("HOLIDAYS"),
    CATEGORY_PERSONAL_CARE("PERSONAL CARE"),
    CATEGORY_SHOPPING("SHOPPING");

    final String name;

    ECategory(String name)
    {
        this.name = name;
    }

    public static Boolean exists(String name)
    {
        if(StringUtils.isBlank(name)) {
            return false;
        }

        return Arrays.stream(ECategory.values())
                .anyMatch(category -> category.name.equals(name.toUpperCase()));
    }

    public static ECategory getByName(String name)
    {
        return Arrays.stream(ECategory.values())
                .filter(
                        eCategory -> eCategory.name.equals(name.toUpperCase())
                )
                .findFirst()
                .orElse(null);
    }
}
