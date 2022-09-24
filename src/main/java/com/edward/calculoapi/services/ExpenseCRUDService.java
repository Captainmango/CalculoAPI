package com.edward.calculoapi.services;

import com.edward.calculoapi.api.dto.requests.CreateExpenseRequest;
import com.edward.calculoapi.api.dto.requests.UpdateExpenseRequest;
import com.edward.calculoapi.database.repositories.CategoryRepository;
import com.edward.calculoapi.database.repositories.ExpenseRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.api.models.Category;
import com.edward.calculoapi.api.models.ECategory;
import com.edward.calculoapi.api.models.Expense;
import com.edward.calculoapi.api.models.User;
import com.edward.calculoapi.exceptions.AuthException;
import com.edward.calculoapi.exceptions.ResourceNotFoundErrorException;
import com.edward.calculoapi.security.services.AuthenticationFacade;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExpenseCRUDService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ExpenseRepository expenseRepository;

    private final AuthenticationFacade auth;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    public ExpenseCRUDService(ExpenseRepository expenseRepository, AuthenticationFacade auth, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.auth = auth;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

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
                logger.warn("A user tried to update an expense they cannot. User: {}", user);
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
        return expenseRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundErrorException("Cannot find expense with id " + id)
        );
    }

    public ResponseEntity<?> deleteExpenseForUserById(long id)
    {
        //@TODO: fix the leaky abstraction. Use the UserCRUDService instead of the repo
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();
        Expense expense = expenseRepository.findById(id).orElseThrow();

        if (expense.getUser().getId() != user.getId()) {
            logger.error("Unable to delete expense. Expense: {} User: {}", expense, user);
            return ResponseEntity.badRequest().body("Transaction not deleted");
        }

        expenseRepository.delete(expense);
        return ResponseEntity.ok().body("Transaction with id " + expense.getId() + " has been deleted");
    }

    private Set<Category> setCategoriesForExpense(Set<String> categories)
    {
        Set<Category> expenseCategories = new HashSet<>();

        //@TODO: Create a category service and start using streams. Also, remove this method as it breaks domain boundaries
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
