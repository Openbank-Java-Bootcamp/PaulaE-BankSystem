package com.ironhack.midterm.Service.Implementation.Users;

import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public void save(Role role){
        roleRepository.save(role);
    }
}
