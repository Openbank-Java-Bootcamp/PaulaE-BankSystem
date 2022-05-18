package com.ironhack.midterm.DTO.AccountsDTO;

import com.ironhack.midterm.DTO.UsersDTO.AccountHolderDTO;
import com.ironhack.midterm.Model.Class.Money;
import jakarta.validation.constraints.NotNull;

public class CheckingDTO {
    @NotNull
    private Money balance;
    @NotNull
    private AccountHolderDTO primaryOwner;

    private AccountHolderDTO secondaryOwner;
    @NotNull
    private String secretKey;

    public CheckingDTO(Money balance, AccountHolderDTO primaryOwner, AccountHolderDTO secondaryOwner, String secretKey) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.secretKey = secretKey;
    }

    public CheckingDTO() {
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public AccountHolderDTO getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolderDTO primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolderDTO getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolderDTO secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
