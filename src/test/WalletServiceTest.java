package com.finance;

import com.finance.model.*;
import com.finance.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletServiceTest {
    private WalletService walletService;
    private User testUser;

    @BeforeEach
    void setUp() {
        walletService = new WalletService();
        testUser = new User("testuser", "password");
        walletService.setCurrentUser(testUser);
    }

    @Test
    void testAddIncome() {
        walletService.addIncome("Зарплата", 50000, "Аванс");
        assertEquals(50000, walletService.getTotalIncome());
    }

    @Test
    void testAddExpense() {
        walletService.addIncome("Зарплата", 50000, "Аванс");
        walletService.addExpense("Еда", 1500, "Продукты");
        assertEquals(1500, walletService.getTotalExpenses());
        assertEquals(48500, walletService.getBalance());
    }

    @Test
    void testSetBudget() {
        walletService.setBudget("Еда", 10000);
        assertEquals(10000, testUser.getWallet().getBudgets().get("Еда"));
    }
}