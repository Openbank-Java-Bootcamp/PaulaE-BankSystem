package com.ironhack.midterm.Controller.Interface.Users;

import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Users.ThirdParty;

import java.math.BigDecimal;

public interface ThirdPartyControllerInterface {
    void saveThridParty(ThridPartyDTO thirdParty);

    void deleteThirdParty(int id);

    void sendMoney(String haskey, String secretkey, int id, BigDecimal amouth);
}
