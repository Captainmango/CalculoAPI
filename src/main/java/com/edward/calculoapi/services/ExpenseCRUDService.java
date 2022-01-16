package com.edward.calculoapi.services;

import com.edward.calculoapi.database.dto.requests.CreateExpenseRequest;
import com.edward.calculoapi.database.dto.requests.UpdateExpenseRequest;
import com.edward.calculoapi.database.repositories.CategoryRepository;
import com.edward.calculoapi.database.repositories.ExpenseRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.exceptions.AuthException;
import com.edward.calculoapi.models.*;
import com.edward.calculoapi.security.services.AuthenticationFacadeImpl;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExpenseCRUDService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private AuthenticationFacadeImpl auth;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Expense> adminGetAllExpenses()
    {
        return expenseRepository.findAll();
    }

    public List<Expense> getAllExpensesForUser(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user found for this ID"));
        return expenseRepository.findByUserIs(user);
    }

    public Expense createExpenseForUser(CreateExpenseRequest createExpenseRequest)
    {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();

        Expense expense = new Expense(
                createExpenseRequest.getTitle(),
                createExpenseRequest.getNotes(),
                createExpenseRequest.getTotal(),
                user
        );
        expense.setCategories(setCategoriesForExpense(createExpenseRequest.getCategories()));
        return expenseRepository.save(expense);
    }

    public Expense updateExpenseForUser(UpdateExpenseRequest updateExpenseRequest)
    {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();

        Expense expense = expenseRepository.findById(updateExpenseRequest.getId()).orElseThrow();
            if (expense.getUser().getId() != user.getId()) {
                throw new AuthException("You aren't authorised to edit this transaction");
            }
            expense.setTitle(updateExpenseRequest.getTitle());
            expense.setNotes(updateExpenseRequest.getNotes());
            expense.setTotal(updateExpenseRequest.getTotal());
            expense.setCategories(setCategoriesForExpense(updateExpenseRequest.getCategories()));

        return expenseRepository.saveAndFlush(expense);
    }

    public Expense geExpenseForUserById(long id)
    {
        return expenseRepository.findById(id).orElseThrow();
    }

    public ResponseEntity<?> deleteExpenseForUserById(long id)
    {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();
        Expense expense = expenseRepository.findById(id).orElseThrow();

        if (expense.getUser().getId() != user.getId()) {
            return ResponseEntity.badRequest().body("Transaction not deleted");
        }

        expenseRepository.delete(expense);
        return ResponseEntity.ok().body("Transaction with id " + expense.getId() + " has been deleted");
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
