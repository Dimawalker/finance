package com.finance.repository;

import com.finance.model.User;
import java.util.*;

public class UserRepository {
    private Map<String, User> users = new HashMap<>();
    private JsonDataManager jsonDataManager;

    public UserRepository(JsonDataManager jsonDataManager) {
        this.jsonDataManager = jsonDataManager;
        loadUsers();
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public void save(User user) {
        users.put(user.getUsername(), user);
        saveUsers();
    }

    private void loadUsers() {
        List<User> loadedUsers = jsonDataManager.loadUsers();
        for (User user : loadedUsers) {
            users.put(user.getUsername(), user);
        }
    }

    private void saveUsers() {
        jsonDataManager.saveUsers(new ArrayList<>(users.values()));
    }
}