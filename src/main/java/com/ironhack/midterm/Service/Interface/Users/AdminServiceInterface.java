package com.ironhack.midterm.Service.Interface.Users;

import com.ironhack.midterm.DTO.UsersDTO.AdminDTO;

public interface AdminServiceInterface {
    void saveAdmin(AdminDTO adminDTO);

    void deleteAdmin(int id);
}
