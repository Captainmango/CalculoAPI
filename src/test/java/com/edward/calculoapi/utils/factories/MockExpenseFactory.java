package com.edward.calculoapi.utils.factories;

import com.edward.calculoapi.api.models.Category;
import com.edward.calculoapi.api.models.ECategory;
import com.edward.calculoapi.database.repositories.CategoryRepository;
import com.edward.calculoapi.database.repositories.ExpenseRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.api.models.Expense;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class MockExpenseFactory {

    private final Faker faker = new Faker(new Locale("en-GB"));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Expense makeExpense(long userId)
    {
        var user = userRepository.findById(userId).orElseThrow();
        Expense expense = new Expense(
                faker.lordOfTheRings().location(),
                faker.lorem().paragraph(2),
                (float) faker.number().randomDouble(2,3,7),
                user
        );

        Set<Category> categories = categoryRepository.findByNameIn(
                List.of(
                        ECategory.CATEGORY_ENTERTAINMENT,
                        ECategory.CATEGORY_PERSONAL_CARE
                )
        );

        expense.setCategories(categories);

        return expenseRepository.save(expense);
    }
}
