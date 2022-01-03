package com.edward.calculoapi.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="transaction")
@Table(name="transactions")
public class Transaction {

    @Id
    @GeneratedValue
    @Column(
            name="id",
            nullable = false,
            updatable = false
    )
    private long Id;

    @Column(
            name="title",
            nullable = false
    )
    private String title;

    @Column(
            name="notes",
            columnDefinition = "TEXT"
    )
    private String notes;

    @Column(
            name="total",
            nullable = false,
            precision = 2,
            length = 10
    )
    private float total;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "transaction_categories",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Transaction(String title, String notes, float total,  User user) {
        this.title = title;
        this.notes = notes;
        this.total = total;
        this.user = user;
    }

    public Transaction() {
    }

    public long getId() {
        return Id;
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
