package com.edward.calculoapi.database.repositories;

import com.edward.calculoapi.api.models.Expense;
import com.edward.calculoapi.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserIs(User user);

}