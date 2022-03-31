package com.edward.calculoapi.api.mappers;

import com.edward.calculoapi.api.dto.CategoryDto;
import com.edward.calculoapi.models.Category;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public static Set<CategoryDto> categoriesToDto(Set<Category> categories)
    {
        return categories.stream().map( c -> new CategoryDto(c.getName().name())
        ).collect(Collectors.toSet());
    }
}
