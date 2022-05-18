package com.ironhack.midterm.Controller.Interface.Users;

import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Users.ThirdParty;

public interface ThirdPartyControllerInterface {
    void saveThridParty(ThridPartyDTO thirdParty);

    void deleteThirdParty(int id);
}
