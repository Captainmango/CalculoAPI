package com.edward.calculoapi.api.dto.shared;

public class CategoryDto {
    private final String name;

    public CategoryDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
