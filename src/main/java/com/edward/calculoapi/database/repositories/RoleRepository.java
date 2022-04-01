package com.edward.calculoapi.database.repositories;

import com.edward.calculoapi.database.models.ERole;
import com.edward.calculoapi.database.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
