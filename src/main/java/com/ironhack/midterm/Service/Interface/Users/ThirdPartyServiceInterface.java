package com.ironhack.midterm.Service.Interface.Users;

import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Users.ThirdParty;

import java.math.BigDecimal;

public interface ThirdPartyServiceInterface {

    void saveThridParty(ThridPartyDTO thirdParty);

    void deleteThridParty(int id);

    boolean getByName(String name);
    void sendMoney(int id, String hashkey, String TPuserName, String acKey, BigDecimal ammout);
}
