package com.ironhack.midterm.Service.Implementation.Users;

import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Model.Users.User;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import com.ironhack.midterm.Repository.Users.ThirdPartyRepository;
import com.ironhack.midterm.Repository.Users.UserRepository;
import com.ironhack.midterm.Service.Interface.Users.ThirdPartyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


@Service
public class ThirdPartyService implements ThirdPartyServiceInterface {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;
    public void saveThridParty(ThridPartyDTO thirdParty){
        Optional<ThirdParty> thirdPartyDB = thirdPartyRepository.findByName(thirdParty.getName());
        User userSameUserName = null;
        if (userRepository.findAll().size() != 0) {
            userSameUserName = userRepository.findByUsername(thirdParty.getUsername());
        }
            if (thirdPartyDB.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A third Party already has that name, remember your password");
            } else {
                if (userSameUserName != null){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username already in use try other.");
                }
                Optional<Role> role = roleRepository.findByName("THIRD_PARTY");
                Collection<Role> roles = new ArrayList<>();
                if (role.isEmpty()) {
                    Role r = new Role("THIRD_PARTY");
                    roles.add(r);
                    roleRepository.save(r);
                } else {
                    roles.add(role.get());
                }
                String keyEncode = passwordEncoder.encode(thirdParty.getHashedKey());
                String KEY = passwordEncoder.encode(thirdParty.getPassword());
                ThirdParty newThridParty = new ThirdParty(thirdParty.getName(), roles, keyEncode, KEY, thirdParty.getUsername());
                thirdPartyRepository.save(newThridParty);
            }
        }


    public void deleteThridParty(int id) {
        Optional<ThirdParty> thirdPartyDB = thirdPartyRepository.findById(id);
        if (thirdPartyDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A third Party already has that name, remember your password");
        }else{
            thirdPartyRepository.deleteById(id);
        }
    }
}
