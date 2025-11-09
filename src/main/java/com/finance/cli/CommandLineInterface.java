package com.finance.cli;

import com.finance.service.*;
import com.finance.repository.*;
import java.util.*;

public class CommandLineInterface {
    private AuthService authService;
    private WalletService walletService;
    private Scanner scanner;
    private boolean running;

    public CommandLineInterface() {
        JsonDataManager jsonDataManager = new JsonDataManager();
        UserRepository userRepository = new UserRepository(jsonDataManager);
        this.authService = new AuthService(userRepository);
        this.walletService = new WalletService();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public void start() {
        System.out.println("=== СИСТЕМА УПРАВЛЕНИЯ ЛИЧНЫМИ ФИНАНСАМИ ===");
        System.out.println("Для справки введите 'help' в любом меню");

        while (running) {
            if (!authService.isAuthenticated()) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showAuthMenu() {
        System.out.println("\n=== АВТОРИЗАЦИЯ ===");
        System.out.println("1. Вход в систему");
        System.out.println("2. Регистрация нового пользователя");
        System.out.println("3. Выход из приложения");
        System.out.println("4. Справка (help)");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine().trim().toLowerCase();
        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "3":
                running = false;
                System.out.println("До свидания!");
                break;
            case "4":
            case "help":
                showAuthHelp();
                break;
            default:
                System.out.println("Неверная команда. Введите 'help' для справки.");
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
        System.out.println("1. Добавить доход");
        System.out.println("2. Добавить расход");
        System.out.println("3. Установить бюджет");
        System.out.println("4. Показать статистику");
        System.out.println("5. Проверить оповещения");
        System.out.println("6. Справка (help)");
        System.out.println("7. Выход из системы");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine().trim().toLowerCase();
        switch (choice) {
            case "1":
                addIncome();
                break;
            case "2":
                addExpense();
                break;
            case "3":
                setBudget();
                break;
            case "4":
                showStatistics();
                break;
            case "5":
                checkAlerts();
                break;
            case "6":
            case "help":
                showMainHelp();
                break;
            case "7":
                authService.logout();
                walletService.setCurrentUser(null);
                System.out.println("Вы вышли из системы.");
                break;
            default:
                System.out.println("Неверная команда. Введите 'help' для справки.");
        }
    }

    private void showAuthHelp() {
        System.out.println("\n=== СПРАВКА - АВТОРИЗАЦИЯ ===");
        System.out.println("Доступные команды:");
        System.out.println("• 1 или 'login' - Вход в систему");
        System.out.println("• 2 или 'register' - Регистрация нового пользователя");
        System.out.println("• 3 или 'exit' - Выход из приложения");
        System.out.println("• 4 или 'help' - Показать эту справку");
        System.out.println("\nДля использования:");
        System.out.println("1. Если у вас есть аккаунт - выберите '1' и введите логин/пароль");
        System.out.println("2. Если вы новый пользователь - выберите '2' для регистрации");
        System.out.println("3. Данные автоматически сохраняются при выходе");
    }

    private void showMainHelp() {
        System.out.println("\n=== СПРАВКА - ОСНОВНЫЕ КОМАНДЫ ===");
        System.out.println("Доступные команды:");
        System.out.println("• 1 - Добавить доход (зарплата, подарки и т.д.)");
        System.out.println("• 2 - Добавить расход (еда, транспорт, развлечения)");
        System.out.println("• 3 - Установить бюджет для категории");
        System.out.println("• 4 - Показать статистику и отчеты");
        System.out.println("• 5 - Проверить оповещения о бюджете");
        System.out.println("• 6 или 'help' - Показать эту справку");
        System.out.println("• 7 - Выйти из системы");

        System.out.println("\n Примеры использования:");
        System.out.println("• Добавление дохода:");
        System.out.println("  Категория: Зарплата");
        System.out.println("  Сумма: 50000");
        System.out.println("  Описание: Аванс за ноябрь");

        System.out.println("• Добавление расхода:");
        System.out.println("  Категория: Еда");
        System.out.println("  Сумма: 1500");
        System.out.println("  Описание: Супермаркет");

        System.out.println("• Установка бюджета:");
        System.out.println("  Категория: Развлечения");
        System.out.println("  Бюджет: 5000");

        System.out.println("\n Особенности:");
        System.out.println("• Категории создаются автоматически при добавлении операций");
        System.out.println("• Система предупредит при превышении бюджета");
        System.out.println("• Данные сохраняются автоматически в формате JSON");
        System.out.println("• Поддерживается несколько пользователей");
    }

    private void login() {
        System.out.print("Логин: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        if (authService.login(username, password)) {
            walletService.setCurrentUser(authService.getCurrentUser());
            System.out.println("Успешный вход! Добро пожаловать, " + username + "!");
        } else {
            System.out.println("Неверный логин или пароль!");
        }
    }

    private void register() {
        System.out.print("Логин: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        if (authService.register(username, password)) {
            walletService.setCurrentUser(authService.getCurrentUser());
            System.out.println("Регистрация успешна! Добро пожаловать, " + username + "!");
        } else {
            System.out.println("Пользователь с таким логином уже существует!");
        }
    }

    private void addIncome() {
        try {
            System.out.print("Категория дохода: ");
            String category = scanner.nextLine();
            System.out.print("Сумма: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Описание: ");
            String description = scanner.nextLine();

            walletService.addIncome(category, amount, description);
            System.out.println("Доход добавлен!");
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат суммы! Используйте числа (например: 1500.50)");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void addExpense() {
        try {
            System.out.print("Категория расхода: ");
            String category = scanner.nextLine();
            System.out.print("Сумма: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Описание: ");
            String description = scanner.nextLine();

            walletService.addExpense(category, amount, description);
            System.out.println("Расход добавлен!");
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат суммы! Используйте числа (например: 1500.50)");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void setBudget() {
        try {
            System.out.print("Категория: ");
            String category = scanner.nextLine();
            System.out.print("Бюджет: ");
            double amount = Double.parseDouble(scanner.nextLine());

            walletService.setBudget(category, amount);
            System.out.println("Бюджет установлен!");
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат суммы!");
        }
    }

    private void showStatistics() {
        double totalIncome = walletService.getTotalIncome();
        double totalExpenses = walletService.getTotalExpenses();
        double balance = totalIncome - totalExpenses;

        System.out.println("\n" + "=".repeat(50));
        System.out.println("СТАТИСТИКА");
        System.out.println("=".repeat(50));

        // Общая информация
        System.out.printf("Общий доход:   %,12.2f руб.%n", totalIncome);
        System.out.printf("Общие расходы: %,12.2f руб.%n", totalExpenses);
        System.out.printf(" Баланс:        %,12.2f руб.%n", balance);
        System.out.println("-".repeat(50));

        // Доходы по категориям
        System.out.println("📈 ДОХОДЫ ПО КАТЕГОРИЯМ:");
        Map<String, Double> incomeByCategory = walletService.getIncomeByCategory();
        if (incomeByCategory.isEmpty()) {
            System.out.println("   Нет данных о доходах");
        } else {
            incomeByCategory.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry ->
                            System.out.printf("   %-20s %,10.2f руб.%n",
                                    entry.getKey(), entry.getValue()));
        }

        System.out.println("-".repeat(50));

        // Расходы по категориям с бюджетами
        System.out.println(" РАСХОДЫ И БЮДЖЕТЫ:");
        Map<String, Double> expensesByCategory = walletService.getExpensesByCategory();
        Map<String, Double> budgets = authService.getCurrentUser().getWallet().getBudgets();

        if (expensesByCategory.isEmpty()) {
            System.out.println("   Нет данных о расходах");
        } else {
            expensesByCategory.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> {
                        String category = entry.getKey();
                        double spent = entry.getValue();
                        double budget = budgets.getOrDefault(category, 0.0);
                        double remaining = budget - spent;

                        String status = remaining >= 0 ? "+" : "-";
                        System.out.printf("   %s %-15s %,10.2f / %,10.2f руб. (остаток: %,10.2f)%n",
                                status, category, spent, budget, remaining);
                    });
        }
        System.out.println("=".repeat(50));
    }

    private void checkAlerts() {
        List<String> alerts = walletService.checkBudgetAlerts();
        if (alerts.isEmpty()) {
            System.out.println("Оповещений нет. Все в порядке!");
        } else {
            System.out.println("\nОПОВЕЩЕНИЯ ===");
            alerts.forEach(System.out::println);
        }
    }
}