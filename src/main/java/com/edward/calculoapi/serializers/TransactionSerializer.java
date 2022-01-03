package com.edward.calculoapi.serializers;

import com.edward.calculoapi.models.Transaction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class TransactionSerializer extends JsonSerializer<List<Transaction>> {

    @Override
    public void serialize(
            List<Transaction> transactionList,
            JsonGenerator gen,
            SerializerProvider serializers
    ) throws IOException {
        gen.writeStartArray();
        for(Transaction transaction: transactionList){
            gen.writeObjectField(""+transaction.getId(), transaction);
        }
        gen.writeEndArray();
    }
}
