package com.finance.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private String id;
    private TransactionType type;
    private String category;
    private double amount;
    private String description;
    private LocalDateTime date;

    @JsonCreator
    public Transaction(@JsonProperty("id") String id,
                       @JsonProperty("type") TransactionType type,
                       @JsonProperty("category") String category,
                       @JsonProperty("amount") double amount,
                       @JsonProperty("description") String description,
                       @JsonProperty("date") LocalDateTime date) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date != null ? date : LocalDateTime.now();
    }

    public Transaction(TransactionType type, String category, double amount, String description) {
        this(null, type, category, amount, description, null);
    }

    public String getId() { return id; }
    public TransactionType getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getDate() { return date; }
}