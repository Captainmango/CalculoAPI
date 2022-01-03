package com.edward.calculoapi.utils;

import com.edward.calculoapi.database.repositories.TransactionRepository;
import com.edward.calculoapi.database.repositories.UserRepository;
import com.edward.calculoapi.models.Transaction;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MockTransactionFactory {

    private final Faker faker = new Faker(new Locale("en-GB"));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction makeTransaction(long userId)
    {
        Transaction transaction = new Transaction(
                faker.lordOfTheRings().location(),
                faker.lorem().paragraph(2),
                (float) faker.number().randomDouble(2,3,7),
                userRepository.findById(userId).get());

        return transactionRepository.save(transaction);
    }

}
