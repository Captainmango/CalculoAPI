package com.edward.calculoapi.controllers;

import com.edward.calculoapi.database.dto.requests.CreateTransactionRequest;
import com.edward.calculoapi.database.dto.requests.UpdateTransactionRequest;
import com.edward.calculoapi.models.Transaction;
import com.edward.calculoapi.security.services.AuthenticationFacade;
import com.edward.calculoapi.security.services.AuthenticationFacadeImpl;
import com.edward.calculoapi.security.services.UserDetailsImpl;
import com.edward.calculoapi.serializers.TransactionSerializer;
import com.edward.calculoapi.services.TransactionCRUDService;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jdk.jfr.ContentType;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "${edward.app.requestOrigin}", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    @Autowired
    private TransactionCRUDService transactionService;

    @Autowired
    AuthenticationFacadeImpl auth;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/transactions")
    public ResponseEntity<?> adminAllUserTransactions()
    {
        return ResponseEntity.ok(transactionService.adminGetAllTransactions());
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(path="/transactions", produces="application/json")
    public ResponseEntity<?> getAllCurrentUserTransactions() {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        List<Transaction> transactions = transactionService.getAllTransactionsForUser(currentUser.getEmail());
        return ResponseEntity.ok(transactions);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(path="/transactions")
    public ResponseEntity<?> createTransactionForLoggedInUser(
            @Valid @RequestBody CreateTransactionRequest createTransactionRequest
    ) {
        Transaction transaction = transactionService.createTransactionForUser(createTransactionRequest);
        return ResponseEntity.accepted().body(transaction);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping(path = "/transactions/{id}")
    public ResponseEntity<?> updateTransactionForUser (
            @PathVariable long id,
            @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest
    ) {
        if (id != updateTransactionRequest.getId()) {
            return ResponseEntity.badRequest().body("Your update request could not be completed.");
        }

        Transaction transaction = transactionService.updateTransactionForUser(updateTransactionRequest);
        return ResponseEntity.accepted().body(transaction);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(path = "/transactions/{id}")
    public ResponseEntity<?> getTransactionForUser (
            @PathVariable long id
    ) {
        Transaction transaction = transactionService.getTransactionForUser(id);
        return ResponseEntity.ok().body(transaction);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping(path = "/transactions/{id}")
    public ResponseEntity<?> deleteTransactionForUser (
            @PathVariable long id
    ) {
        return transactionService.deleteTransactionForUser(id);
    }

}
