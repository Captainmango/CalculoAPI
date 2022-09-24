package com.edward.calculoapi.api.mappers;

import com.edward.calculoapi.api.dto.responses.ExpenseResponse;
import com.edward.calculoapi.api.models.Expense;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpenseMapper {
    public List<ExpenseResponse> expenseListToDto(List<Expense> expenseList)
    {
        return expenseList.stream().map(ExpenseResponse::new).collect(Collectors.toList());
    }

    public ExpenseResponse expenseToDto(Expense expense)
    {
        return new ExpenseResponse(expense);
    }
}
