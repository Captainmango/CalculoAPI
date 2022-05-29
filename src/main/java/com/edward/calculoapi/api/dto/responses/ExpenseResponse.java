package com.edward.calculoapi.api.dto.responses;

import com.edward.calculoapi.api.dto.shared.CategoryDto;
import com.edward.calculoapi.api.mappers.CategoryMapper;
import com.edward.calculoapi.database.models.Expense;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class ExpenseResponse {

    private long Id;
    private String title;
    private String notes;
    private float total;
    private Set<CategoryDto> categories = new HashSet<>();
    private Instant createdAt;
    private Instant updatedAt;

    public ExpenseResponse(Expense expense) {
        Id = expense.getId();
        this.title = expense.getTitle();
        this.notes = expense.getNotes();
        this.total = expense.getTotal();
        this.createdAt = expense.getCreatedAt();
        this.updatedAt = expense.getCreatedAt();
        this.categories = CategoryMapper.categoriesToDto(expense.getCategories());
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Set<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDto> categories) {
        this.categories = categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
