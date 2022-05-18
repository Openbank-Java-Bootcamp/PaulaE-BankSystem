package com.ironhack.midterm.DTO.UsersDTO;

import com.ironhack.midterm.Model.Class.Address;
import jakarta.validation.constraints.NotNull;

public class AccountHolderDTO {

    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String dateOfBirth;
    @NotNull
    private Address primaryAddress;

    private String mailAddress;

    @NotNull
    private String password;

    public AccountHolderDTO(String name, String dateOfBirth, Address primaryAddress, String mailAddress, String password, String username) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailAddress = mailAddress;
        this.password = password;
        this.username = username;
    }

    public AccountHolderDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
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
