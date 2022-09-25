package com.edward.calculoapi.services;

import com.edward.calculoapi.api.dto.requests.CreateExpenseRequest;
import com.edward.calculoapi.api.dto.requests.UpdateExpenseRequest;
import com.edward.calculoapi.api.models.*;
import com.edward.calculoapi.database.repositories.CategoryRepository;
import com.edward.calculoapi.database.repositories.ExpenseRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.exceptions.AuthException;
import com.edward.calculoapi.exceptions.ResourceNotFoundErrorException;
import com.edward.calculoapi.security.services.AuthenticationFacade;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> {
                    logger.error("[ExpenseCRUDService@getAllExpensesForUser] user does not exist by email" +
                            "email: {}", email);
                    return new UsernameNotFoundException("No user found");
                }
        );

        return expenseRepository.findByUserIs(user);
    }

    public Expense createExpenseForUser(CreateExpenseRequest createExpenseRequest)
    {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(
                () -> {
                    logger.error("[ExpenseCRUDService@getAllExpensesForUser] user does not exist by ID. " +
                            "ID: {}", currentUser.getId());
                    return new UsernameNotFoundException("No user found for this ID");
                }
        );

        Expense expense = new Expense(
                createExpenseRequest.getTitle(),
                createExpenseRequest.getNotes(),
                createExpenseRequest.getTotal(),
                user
        );

        var categories = findAllCategories(createExpenseRequest.getCategories());

        // @TODO: Look at using the flyweight or proxy pattern here. DB access for this is deffo overkill
        expense.setCategories(categoryRepository.findByNameIn(categories));

        return expenseRepository.save(expense);
    }

    public Expense updateExpenseForUser(long id, UpdateExpenseRequest updateExpenseRequest)
    {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(
                        () -> {
                            logger.error("[ExpenseCRUDService@updateExpenseForUser] failed to find user. " +
                                    "User ID: {}", currentUser.getId());
                            return new UsernameNotFoundException("No user found for this ID");
                        }
                );

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(
                        () -> {
                            logger.error("[ExpenseCRUDService@updateExpenseForUser] failed to find expense" +
                                    "Expense ID: {}", updateExpenseRequest.getId());
                            return new ResourceNotFoundErrorException("No expense for this ID");
                        }
                );

        if (expense.getUser().getId() != user.getId()) {
            logger.warn("A user tried to update an expense they cannot. User: {}", user);
            throw new AuthException("You aren't authorised to edit this transaction");
        }

        expense.setTitle(updateExpenseRequest.getTitle());
        expense.setNotes(updateExpenseRequest.getNotes());
        expense.setTotal(updateExpenseRequest.getTotal());

        // @TODO: Look at using the flyweight or proxy pattern here. DB access for this is deffo overkill
        expense.setCategories(categoryRepository.findByNameIn(findAllCategories(updateExpenseRequest.getCategories())));

        return expenseRepository.saveAndFlush(expense);
    }

    public Expense getExpenseForUserById(long id)
    {
        return expenseRepository.findById(id).orElseThrow(
            () -> {
                logger.error("[ExpenseCRUDService@getExpenseForUserById] cannot find expense by ID. " +
                        "ID: {}", id);
                return new ResourceNotFoundErrorException("Cannot find expense with id " + id);
            }
        );
    }

    public ResponseEntity<?> deleteExpenseForUserById(long id)
    {
        //@TODO: fix the leaky abstraction. Use the UserCRUDService instead of the repo
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(
                () -> {
                    logger.error("[ExpenseCRUDService@getAllExpensesForUser] user does not exist by ID. " +
                            "ID: {}", currentUser.getId());
                    return new UsernameNotFoundException("No user found for this ID");
                }
        );
        Expense expense = expenseRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("[ExpenseCRUDService@updateExpenseForUser] failed to find expense" +
                            "Expense ID: {}", id);
                    return new ResourceNotFoundErrorException("No expense for this ID");
                }
        );

        if (expense.getUser().getId() != user.getId()) {
            logger.error("Unable to delete expense. Expense: {} User: {}", expense, user);
            return ResponseEntity.badRequest().body("Transaction not deleted");
        }

        expenseRepository.delete(expense);
        return ResponseEntity.ok().body("Transaction with id " + expense.getId() + " has been deleted");
    }

    // @TODO: move this out into a category service flyweight thingy
    private List<ECategory> findAllCategories(Set<String> categories)
    {
        return categories.stream()
                .map(ECategory::getByName)
                .collect(Collectors.toList());
    }
}
