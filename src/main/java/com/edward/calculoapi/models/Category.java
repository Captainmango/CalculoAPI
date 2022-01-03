package com.edward.calculoapi.models;

import javax.persistence.*;

@Entity(name="category")
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "name"
    )
    private ECategory name;

    public Category() {
    }

    public Category(ECategory name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public ECategory getName() {
        return name;
    }

    public void setName(ECategory name) {
        this.name = name;
    }
}
