package com.ironhack.midterm.DTO.AccountsDTO;

import com.ironhack.midterm.Model.Class.Money;
import jakarta.validation.constraints.NotNull;

public class BalanceOnlyDTO {

    @NotNull
    public Money balance;

    public BalanceOnlyDTO() {
    }

    public BalanceOnlyDTO(Money balance) {
        this.balance = balance;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }
}
