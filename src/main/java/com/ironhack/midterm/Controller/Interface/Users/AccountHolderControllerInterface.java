package com.ironhack.midterm.Controller.Interface.Users;

import com.ironhack.midterm.Model.Class.Money;

import java.math.BigDecimal;

public interface AccountHolderControllerInterface {

    void transferfunds(String username, int id, BigDecimal quantity);
}
