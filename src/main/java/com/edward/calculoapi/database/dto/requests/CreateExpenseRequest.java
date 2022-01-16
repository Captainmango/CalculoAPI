package com.edward.calculoapi.database.dto.requests;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class CreateExpenseRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String notes;

    private float total;

    public Set<String> getCategories() {
        return categories;
    }

    public CreateExpenseRequest(String title, String notes, float total, Set<String> categories) {
        this.title = title;
        this.notes = notes;
        this.total = total;
        this.categories = categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    private Set<String> categories;

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

    public String toJson() {
        return "{" +
                "title:'" + title + '\'' +
                ", notes:'" + notes + '\'' +
                ", total:" + total +
                ", categories:" + categories +
                '}';
    }
}
