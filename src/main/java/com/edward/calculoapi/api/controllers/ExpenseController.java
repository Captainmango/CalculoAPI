package com.edward.calculoapi.api.controllers;

import com.edward.calculoapi.api.dto.requests.CreateExpenseRequest;
import com.edward.calculoapi.api.dto.requests.UpdateExpenseRequest;
import com.edward.calculoapi.api.dto.responses.ExpenseResponse;
import com.edward.calculoapi.api.mappers.ExpenseMapper;
import com.edward.calculoapi.exceptions.ResourceUpdateErrorException;
import com.edward.calculoapi.security.services.AuthenticationFacade;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import com.edward.calculoapi.services.ExpenseCRUDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "${edward.app.requestOrigin}", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ExpenseController {

    private final ExpenseCRUDService expenseCRUDService;
    private final ExpenseMapper expenseMapper;
    private final AuthenticationFacade auth;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ExpenseController(ExpenseCRUDService expenseCRUDService, ExpenseMapper expenseMapper, AuthenticationFacade auth) {
        this.expenseCRUDService = expenseCRUDService;
        this.expenseMapper = expenseMapper;
        this.auth = auth;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path="/admin/expenses", produces="application/json")
    public ResponseEntity<?> adminAllUserExpenses()
    {
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();

        try {
            expenseResponseList = expenseMapper.expenseListToDto(expenseCRUDService.adminGetAllExpenses());
        } catch(Exception e) {
            logger.error("message: {}", e.getMessage());
            throw new ResourceUpdateErrorException("failed to update expense");
        }

        return ResponseEntity.ok(expenseResponseList);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(path="/expenses", produces="application/json")
    public ResponseEntity<?> getAllCurrentUserExpenses()
    {
        var currentUser = (UserDetailsImpl) auth.getCurrentUser();
        List<ExpenseResponse> expenses = expenseMapper
                .expenseListToDto(
                    expenseCRUDService.getAllExpensesForUser(currentUser.getEmail())
                );

        return ResponseEntity.ok(expenses);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path="/expenses", produces="application/json")
    public ResponseEntity<?> createExpenseForLoggedInUser(
            @Valid @RequestBody CreateExpenseRequest createExpenseRequest
    ) {
        ExpenseResponse expense = expenseMapper
                .expenseToDto(
                    expenseCRUDService.createExpenseForUser(createExpenseRequest)
                );
        return ResponseEntity.ok().body(expense);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping(path = "/expenses/{id}", produces="application/json")
    public ResponseEntity<?> updateExpenseForUser(
            @PathVariable long id,
            @Valid @RequestBody UpdateExpenseRequest updateExpenseRequest
    ) {
        ExpenseResponse expense = expenseMapper.expenseToDto(expenseCRUDService.updateExpenseForUser(id, updateExpenseRequest));
        return ResponseEntity.ok().body(expense);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(path = "/expenses/{id}", produces="application/json")
    public ResponseEntity<?> getExpenseForUser(
            @PathVariable long id
    ) {
        ExpenseResponse expense = expenseMapper.expenseToDto(expenseCRUDService.geExpenseForUserById(id));
        return ResponseEntity.ok().body(expense);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping(path = "/expenses/{id}", produces="application/json")
    public ResponseEntity<?> deleteExpenseForUser(
            @PathVariable long id
    ) {
        return expenseCRUDService.deleteExpenseForUserById(id);
    }

}
