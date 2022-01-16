package com.edward.calculoapi.services;

import com.edward.calculoapi.database.dto.requests.CreateTransactionRequest;
import com.edward.calculoapi.database.dto.requests.UpdateTransactionRequest;
import com.edward.calculoapi.database.repositories.CategoryRepository;
import com.edward.calculoapi.database.repositories.TransactionRepository;
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
public class TransactionCRUDService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthenticationFacadeImpl auth;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Transaction> adminGetAllTransactions()
    {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllTransactionsForUser(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user found for this ID"));
        return transactionRepository.findByUserIs(user);
    }

    public Transaction createTransactionForUser(CreateTransactionRequest createTransactionRequest)
    {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();

        Transaction transaction = new Transaction(
                createTransactionRequest.getTitle(),
                createTransactionRequest.getNotes(),
                createTransactionRequest.getTotal(),
                user
        );
        transaction.setCategories(setCategoriesForTransaction(createTransactionRequest.getCategories()));
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransactionForUser(UpdateTransactionRequest updateTransactionRequest)
    {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();

        Transaction transaction = transactionRepository.findById(updateTransactionRequest.getId()).orElseThrow();
            if (transaction.getUser().getId() != user.getId()) {
                throw new AuthException("You aren't authorised to edit this transaction");
            }
            transaction.setTitle(updateTransactionRequest.getTitle());
            transaction.setNotes(updateTransactionRequest.getNotes());
            transaction.setTotal(updateTransactionRequest.getTotal());
            transaction.setCategories(setCategoriesForTransaction(updateTransactionRequest.getCategories()));

        return transactionRepository.saveAndFlush(transaction);
    }

    public Transaction getTransactionForUserById(long id)
    {
        return transactionRepository.findById(id).orElseThrow();
    }

    public ResponseEntity<?> deleteTransactionForUserById(long id)
    {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();
        Transaction transaction = transactionRepository.findById(id).orElseThrow();

        if (transaction.getUser().getId() != user.getId()) {
            return ResponseEntity.badRequest().body("Transaction not deleted");
        }

        transactionRepository.delete(transaction);
        return ResponseEntity.ok().body("Transaction with id " + transaction.getId() + " has been deleted");
    }

    private Set<Category> setCategoriesForTransaction(Set<String> categories)
    {
        Set<Category> transactionCategories = new HashSet<>();

        for(String category : categories) {
            for (ECategory categoryType : ECategory.values()) {
                if (ECategory.exists(category.toUpperCase())) {
                    Category categoryToAdd = categoryRepository.findByName(ECategory.getByName(category)).orElseThrow();
                    transactionCategories.add(categoryToAdd);
                }
                break;
            }
        }

        return transactionCategories;
    }

}
