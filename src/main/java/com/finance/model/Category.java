package com.finance.model;

public class Category {
    private String name;
    private CategoryType type;

    public Category(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

    // геттеры
    public String getName() { return name; }
    public CategoryType getType() { return type; }
}

enum CategoryType {
    INCOME, EXPENSE
}