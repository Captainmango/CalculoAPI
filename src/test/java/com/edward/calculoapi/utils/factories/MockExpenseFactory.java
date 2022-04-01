package com.edward.calculoapi.utils.factories;

import com.edward.calculoapi.database.models.Category;
import com.edward.calculoapi.database.models.ECategory;
import com.edward.calculoapi.database.repositories.CategoryRepository;
import com.edward.calculoapi.database.repositories.ExpenseRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.database.models.Expense;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
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
        Expense expense = new Expense(
                faker.lordOfTheRings().location(),
                faker.lorem().paragraph(2),
                (float) faker.number().randomDouble(2,3,7),
                userRepository.findById(userId).get()
        );

        expense.setCategories(setCategoriesForExpense(Set.of("ENTERTAINMENT", "FAMILY")));

        return expenseRepository.save(expense);
    }

    private Set<Category> setCategoriesForExpense(Set<String> categories)
    {
        Set<Category> expenseCategories = new HashSet<>();

        for(String category : categories) {
            for (ECategory categoryType : ECategory.values()) {
                if (ECategory.exists(category.toUpperCase())) {
                    Category categoryToAdd = categoryRepository.findByName(ECategory.getByName(category)).orElseThrow();
                    expenseCategories.add(categoryToAdd);
                }
                break;
            }
        }

        return expenseCategories;
    }
}
