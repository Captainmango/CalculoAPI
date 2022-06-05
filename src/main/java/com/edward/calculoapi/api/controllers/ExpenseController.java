package com.edward.calculoapi.api.controllers;

import com.edward.calculoapi.api.dto.requests.CreateExpenseRequest;
import com.edward.calculoapi.api.dto.requests.UpdateExpenseRequest;
import com.edward.calculoapi.api.dto.responses.ExpenseResponse;
import com.edward.calculoapi.api.mappers.ExpenseMapper;
import com.edward.calculoapi.security.services.AuthenticationFacade;
import com.edward.calculoapi.security.services.AuthenticationFacadeImpl;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import com.edward.calculoapi.services.ExpenseCRUDService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "${edward.app.requestOrigin}", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ExpenseController {

    private final ExpenseCRUDService expenseCRUDService;
    private final ExpenseMapper expenseMapper;
    private final AuthenticationFacade auth;

    public ExpenseController(ExpenseCRUDService expenseCRUDService, ExpenseMapper expenseMapper, AuthenticationFacadeImpl auth) {
        this.expenseCRUDService = expenseCRUDService;
        this.expenseMapper = expenseMapper;
        this.auth = auth;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/expenses")
    public ResponseEntity<?> adminAllUserExpenses()
    {
        List<ExpenseResponse> expenseResponseList = expenseMapper.expenseListToDto(expenseCRUDService.adminGetAllExpenses());

        return ResponseEntity.ok(expenseResponseList);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(path="/expenses", produces="application/json")
    public ResponseEntity<?> getAllCurrentUserExpenses() {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        List<ExpenseResponse> expenses = expenseMapper.expenseListToDto(expenseCRUDService.getAllExpensesForUser(currentUser.getEmail()));

        return ResponseEntity.ok(expenses);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path="/expenses")
    public ResponseEntity<?> createExpenseForLoggedInUser(
            @Valid @RequestBody CreateExpenseRequest createExpenseRequest
    ) {
        ExpenseResponse expense = expenseMapper.expenseToDto(expenseCRUDService.createExpenseForUser(createExpenseRequest));
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

        ExpenseResponse expense = expenseMapper.expenseToDto(expenseCRUDService.updateExpenseForUser(updateExpenseRequest));
        return ResponseEntity.accepted().body(expense);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(path = "/expenses/{id}")
    public ResponseEntity<?> getExpenseForUser (
            @PathVariable long id
    ) {
        ExpenseResponse expense = expenseMapper.expenseToDto(expenseCRUDService.geExpenseForUserById(id));
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
