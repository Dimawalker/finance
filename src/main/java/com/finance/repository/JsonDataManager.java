package com.finance.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finance.model.User;
import java.io.*;
import java.util.*;

public class JsonDataManager {
    private static final String DATA_FILE = "finance_data.json";
    private final ObjectMapper objectMapper;

    public JsonDataManager() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
    }

    public List<User> loadUsers() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        } catch (IOException e) {
            System.out.println("Ошибка загрузки данных: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveUsers(List<User> users) {
        try {
            objectMapper.writeValue(new File(DATA_FILE), users);
            System.out.println("Данные успешно сохранены в " + DATA_FILE);
        } catch (IOException e) {
            System.out.println(" Ошибка сохранения данных: " + e.getMessage());
        }
    }
}