package com.ironhack.midterm.Model.Users;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class ThirdParty extends User {
    @NotNull
    private String hashedKey;

    public ThirdParty() {
    }

    public ThirdParty(String name, Collection<Role> roles, String hashedKey, String password,String username) {
        super(name, roles, password, username);
        this.hashedKey = hashedKey;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
