package com.edward.calculoapi.api.controllers;

import com.edward.calculoapi.api.dto.requests.CreateExpenseRequest;
import com.edward.calculoapi.api.dto.requests.UpdateExpenseRequest;
import com.edward.calculoapi.models.Expense;
import com.edward.calculoapi.security.services.AuthenticationFacadeImpl;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import com.edward.calculoapi.services.ExpenseCRUDService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "${edward.app.requestOrigin}", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ExpenseController {

    @Autowired
    private ExpenseCRUDService expenseCRUDService;

    @Autowired
    AuthenticationFacadeImpl auth;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/expenses")
    public ResponseEntity<?> adminAllUserExpenses()
    {
        return ResponseEntity.ok(expenseCRUDService.adminGetAllExpenses());
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(path="/expenses", produces="application/json")
    public ResponseEntity<?> getAllCurrentUserExpenses() {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        List<Expense> expenses = expenseCRUDService.getAllExpensesForUser(currentUser.getEmail());

        return ResponseEntity.ok(expenses);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path="/expenses")
    public ResponseEntity<?> createExpenseForLoggedInUser(
            @Valid @RequestBody CreateExpenseRequest createExpenseRequest
    ) {
        Expense expense = expenseCRUDService.createExpenseForUser(createExpenseRequest);
        return ResponseEntity.accepted().body(expense);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping(path = "/expenses/{id}")
    public ResponseEntity<?> updateExpenseForUser (
            @PathVariable long id,
            @Valid @RequestBody UpdateExpenseRequest updateExpenseRequest
    ) {
        if (id != updateExpenseRequest.getId()) {
            return ResponseEntity.badRequest().body("Your update request could not be completed.");
        }

        Expense expense = expenseCRUDService.updateExpenseForUser(updateExpenseRequest);
        return ResponseEntity.accepted().body(expense);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(path = "/expenses/{id}")
    public ResponseEntity<?> getExpenseForUser (
            @PathVariable long id
    ) {
        Expense expense = expenseCRUDService.geExpenseForUserById(id);
        return ResponseEntity.ok().body(expense);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping(path = "/expenses/{id}")
    public ResponseEntity<?> deleteExpenseForUser (
            @PathVariable long id
    ) {
        return expenseCRUDService.deleteExpenseForUserById(id);
    }

}
