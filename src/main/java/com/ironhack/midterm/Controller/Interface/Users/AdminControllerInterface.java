package com.ironhack.midterm.Controller.Interface.Users;

import com.ironhack.midterm.DTO.UsersDTO.AdminDTO;

public interface AdminControllerInterface {
    void saveAdmin(AdminDTO adminDTO);

    void deleteAdmin(int id);
}
