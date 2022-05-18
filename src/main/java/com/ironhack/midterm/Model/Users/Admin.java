package com.ironhack.midterm.Model.Users;

import jakarta.persistence.Entity;

import java.util.Collection;

@Entity
public class Admin extends User {

    public Admin() {
    }

    public Admin(String name, Collection<Role> roles, String password, String username) {
        super(name, roles, password, username);
    }



}
