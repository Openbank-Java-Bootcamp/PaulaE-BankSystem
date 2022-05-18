package com.ironhack.midterm.DTO.AccountsDTO;

import com.ironhack.midterm.DTO.UsersDTO.AccountHolderDTO;
import com.ironhack.midterm.Model.Class.Money;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SavingDTO {
    @NotNull
    private Money balance;
    @NotNull
    private AccountHolderDTO primaryOwner;
    private AccountHolderDTO secondaryOwner;
    @NotNull
    private String secretKey;
    private BigDecimal interestRate;


    public SavingDTO(Money balance, AccountHolderDTO primaryOwner, AccountHolderDTO secondaryOwner, String secretKey, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
    }

    public SavingDTO() {
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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
