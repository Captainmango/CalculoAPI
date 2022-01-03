package com.edward.calculoapi.database.dto.requests;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class UpdateTransactionRequest {

    @NotNull
    private long id;
    private String title;
    private String notes;
    private float total;
    private Set<String> categories;

    public UpdateTransactionRequest(long id, String title, String notes, float total, Set<String> categories) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.total = total;
        this.categories = categories;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
}
