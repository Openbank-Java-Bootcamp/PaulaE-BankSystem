package com.ironhack.midterm.Service.Interface.Users;

import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Users.ThirdParty;

public interface ThirdPartyServiceInterface {

    void saveThridParty(ThridPartyDTO thirdParty);

    void deleteThridParty(int id);
}
