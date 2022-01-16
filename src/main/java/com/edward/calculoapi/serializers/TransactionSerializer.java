package com.edward.calculoapi.serializers;

import com.edward.calculoapi.models.Expense;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class TransactionSerializer extends JsonSerializer<List<Expense>> {

    @Override
    public void serialize(
            List<Expense> expenseList,
            JsonGenerator gen,
            SerializerProvider serializers
    ) throws IOException {
        gen.writeStartArray();
        for(Expense expense : expenseList){
            gen.writeObjectField(""+ expense.getId(), expense);
        }
        gen.writeEndArray();
    }
}
