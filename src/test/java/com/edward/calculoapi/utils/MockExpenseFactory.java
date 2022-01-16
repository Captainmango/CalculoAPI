package com.edward.calculoapi.utils;

import com.edward.calculoapi.database.repositories.ExpenseRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.models.Expense;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MockExpenseFactory {

    private final Faker faker = new Faker(new Locale("en-GB"));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense makeTransaction(long userId)
    {
        Expense expense = new Expense(
                faker.lordOfTheRings().location(),
                faker.lorem().paragraph(2),
                (float) faker.number().randomDouble(2,3,7),
                userRepository.findById(userId).get());

        return expenseRepository.save(expense);
    }

}
