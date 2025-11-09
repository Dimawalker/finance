package com.finance.service;

import com.finance.model.User;
import com.finance.repository.UserRepository;

public class AuthService {
    private UserRepository userRepository;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return false; // пользователь уже существует
        }
        User user = new User(username, password);
        userRepository.save(user);
        currentUser = user;
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}