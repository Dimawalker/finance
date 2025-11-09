package com.finance.service;

import com.finance.model.*;
import java.util.*;

public class WalletService {
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void addIncome(String category, double amount, String description) {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        Transaction transaction = new Transaction(TransactionType.INCOME, category, amount, description);
        currentUser.getWallet().addTransaction(transaction);
    }

    public void addExpense(String category, double amount, String description) {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        Transaction transaction = new Transaction(TransactionType.EXPENSE, category, amount, description);
        currentUser.getWallet().addTransaction(transaction);
    }

    public void editBudget(String category, double newAmount) {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        if (currentUser.getWallet().getBudgets().containsKey(category)) {
            currentUser.getWallet().getBudgets().put(category, newAmount);
        } else {
            throw new IllegalArgumentException("Категория '" + category + "' не найдена");
        }
    }

    public void setBudget(String category, double amount) {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        currentUser.getWallet().setBudget(category, amount);
    }

    public double getTotalIncome() {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        return currentUser.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses() {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        return currentUser.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Map<String, Double> getIncomeByCategory() {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        Map<String, Double> result = new HashMap<>();
        currentUser.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .forEach(t -> result.merge(t.getCategory(), t.getAmount(), Double::sum));
        return result;
    }

    public Map<String, Double> getExpensesByCategory() {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        Map<String, Double> result = new HashMap<>();
        currentUser.getWallet().getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .forEach(t -> result.merge(t.getCategory(), t.getAmount(), Double::sum));
        return result;
    }

    public List<String> checkBudgetAlerts() {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");

        List<String> alerts = new ArrayList<>();
        Map<String, Double> expensesByCategory = getExpensesByCategory();
        Map<String, Double> budgets = currentUser.getWallet().getBudgets();

        for (Map.Entry<String, Double> budget : budgets.entrySet()) {
            String category = budget.getKey();
            double budgetAmount = budget.getValue();
            double spent = expensesByCategory.getOrDefault(category, 0.0);

            if (spent > budgetAmount) {
                alerts.add(String.format("ПРЕВЫШЕН БЮДЖЕТ! Категория: %s, Бюджет: %.2f, Потрачено: %.2f",
                        category, budgetAmount, spent));
            } else if (spent >= budgetAmount * 0.8) {
                alerts.add(String.format("ВНИМАНИЕ! Категория: %s, использовано %.0f%% бюджета",
                        category, (spent / budgetAmount) * 100));
            }
        }

        if (getTotalExpenses() > getTotalIncome()) {
            alerts.add("ВНИМАНИЕ! Расходы превышают доходы!");
        }

        return alerts;
    }

    public double getBalance() {
        if (currentUser == null) throw new IllegalStateException("User not authenticated");
        return currentUser.getWallet().getBalance();
    }
}