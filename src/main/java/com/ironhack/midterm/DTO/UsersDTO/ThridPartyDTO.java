package com.ironhack.midterm.DTO.UsersDTO;

import jakarta.validation.constraints.NotNull;

public class ThridPartyDTO {
    @NotNull
    private String name;

    @NotNull
    private String hashedKey;

    @NotNull
    private String password;

    @NotNull
    private String username;


    public ThridPartyDTO() {
    }

    public ThridPartyDTO(String name, String hashedKey, String password, String username) {
        this.name = name;
        this.hashedKey = hashedKey;
        this.password = password;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
