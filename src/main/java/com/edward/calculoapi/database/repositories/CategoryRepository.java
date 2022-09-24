package com.edward.calculoapi.database.repositories;

import com.edward.calculoapi.api.models.Category;
import com.edward.calculoapi.api.models.ECategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(ECategory name);

    Set<Category> findByNameIn(List<ECategory> categoryList);
}