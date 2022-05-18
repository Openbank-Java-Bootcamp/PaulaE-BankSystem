package com.ironhack.midterm.DTO.AccountsDTO;

import com.ironhack.midterm.DTO.UsersDTO.AccountHolderDTO;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreditCardDTO {

    @NotNull
    private Money balance;
    @NotNull
    private AccountHolderDTO primaryOwner;
    private AccountHolderDTO secondaryOwner;
    private Money creditLimit;
    private BigDecimal interestRate;

    public CreditCardDTO() {
    }

    public CreditCardDTO(Money balance, AccountHolderDTO primaryOwner, AccountHolderDTO secondaryOwner, Money creditLimit, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
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

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
