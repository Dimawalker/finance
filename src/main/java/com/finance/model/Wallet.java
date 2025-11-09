package com.finance.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;

public class Wallet {
    private double balance;
    private List<Transaction> transactions;
    private Map<String, Double> budgets;

    @JsonCreator
    public Wallet(@JsonProperty("balance") double balance,
                  @JsonProperty("transactions") List<Transaction> transactions,
                  @JsonProperty("budgets") Map<String, Double> budgets) {
        this.balance = balance;
        this.transactions = transactions != null ? transactions : new ArrayList<>();
        this.budgets = budgets != null ? budgets : new HashMap<>();
    }

    public Wallet() {
        this(0.0, new ArrayList<>(), new HashMap<>());
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        if (transaction.getType() == TransactionType.INCOME) {
            balance += transaction.getAmount();
        } else {
            balance -= transaction.getAmount();
        }
    }

    public void setBudget(String category, double amount) {
        budgets.put(category, amount);
    }

    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; }
    public Map<String, Double> getBudgets() { return budgets; }
}