package com.edward.calculoapi.database.repositories;

import com.edward.calculoapi.models.Category;
import com.edward.calculoapi.models.ECategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(ECategory name);
}