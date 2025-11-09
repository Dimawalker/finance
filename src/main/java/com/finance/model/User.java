package com.finance.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String username;
    private String password;
    private Wallet wallet;

    @JsonCreator
    public User(@JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("wallet") Wallet wallet) {
        this.username = username;
        this.password = password;
        this.wallet = wallet != null ? wallet : new Wallet();
    }

    public User(String username, String password) {
        this(username, password, new Wallet());
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Wallet getWallet() { return wallet; }
}