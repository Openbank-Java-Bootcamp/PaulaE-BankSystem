package com.ironhack.midterm.Service.Implementation.Users;

import com.ironhack.midterm.DTO.UsersDTO.AdminDTO;
import com.ironhack.midterm.Model.Users.Admin;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Model.Users.User;
import com.ironhack.midterm.Repository.Users.AdminRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import com.ironhack.midterm.Repository.Users.UserRepository;
import com.ironhack.midterm.Service.Interface.Users.AdminServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class AdminService implements AdminServiceInterface {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public void saveAdmin(AdminDTO adminDTO){
        Optional<Admin> adminDB = adminRepository.findByName(adminDTO.getName());
        User userSameUserName = null;
        if (userRepository.findAll().size() != 0) {
            userSameUserName = userRepository.findByUsername(adminDTO.getUsername());
        }
            if (adminDB.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An admin already has that name, remember your password");
            } else {
                if (userSameUserName != null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username already in use try other.");
                }
                Optional<Role> role = roleRepository.findByName("ADMIN");
                Collection<Role> roles = new ArrayList<>();
                if (role.isEmpty()) {
                    Role r = new Role("ADMIN");
                    roles.add(r);
                    roleRepository.save(r);
                } else {
                    roles.add(role.get());
                }
                String keyEncode = passwordEncoder.encode(adminDTO.getPassword());
                Admin newAdmin = new Admin(adminDTO.getName(), roles, keyEncode, adminDTO.getUsername());
                adminRepository.save(newAdmin);
            }
        }

    public void deleteAdmin(int id) {
        Optional<Admin> adminDB = adminRepository.findById(id);
        if (!adminDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no admin with such id");
        }else{
            adminRepository.deleteById(id);
        }
    }
}
