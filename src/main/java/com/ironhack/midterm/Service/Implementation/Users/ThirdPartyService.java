package com.ironhack.midterm.Service.Implementation.Users;

import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.CreditCard;
import com.ironhack.midterm.Model.Accounts.Savings;
import com.ironhack.midterm.Model.Accounts.StudentChecking;
import com.ironhack.midterm.Model.Users.Role;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Model.Users.User;
import com.ironhack.midterm.Repository.Accounts.CheckingRepository;
import com.ironhack.midterm.Repository.Accounts.CreditCardRepository;
import com.ironhack.midterm.Repository.Accounts.SavingsRepository;
import com.ironhack.midterm.Repository.Accounts.StudentsCheckingRepository;
import com.ironhack.midterm.Repository.Users.RoleRepository;
import com.ironhack.midterm.Repository.Users.ThirdPartyRepository;
import com.ironhack.midterm.Repository.Users.UserRepository;
import com.ironhack.midterm.Service.Interface.Users.ThirdPartyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
    CheckingRepository checkingRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    StudentsCheckingRepository studentsCheckingRepository;
    @Autowired
    AccountHolderService accountHolderService;

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
        if (!thirdPartyDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A third Party already has that name, remember your password");
        }else{
            thirdPartyRepository.deleteById(id);
        }
    }


    public boolean getByName(String name) {
        Optional<ThirdParty> thirdPartyDB = thirdPartyRepository.findByName(name);
        if (thirdPartyDB.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public void sendMoney(int id, String hashkey, String TPuserName, String acKey, BigDecimal ammout){
        ThirdParty thirdPartyDB = thirdPartyRepository.findByUsername(TPuserName);
        Optional<Checking> checkingDB = checkingRepository.findById(id);
        Optional<Savings> savingsDB = savingsRepository.findById(id);
        Optional<StudentChecking> studentcheckingDB = studentsCheckingRepository.findById(id);
        if(thirdPartyDB != null){
            if (checkingDB.isPresent()) {
                if (passwordEncoder.matches(acKey, checkingDB.get().getSecretKey()) && passwordEncoder.matches(hashkey,thirdPartyDB.getPassword())) {
                    accountHolderService.tranferFundDestinationAccount(id, ammout);
                }
            } else if (savingsDB.isPresent()) {
                if (passwordEncoder.matches(acKey, savingsDB.get().getSecretKey()) && passwordEncoder.matches(hashkey,thirdPartyDB.getPassword())) {
                    accountHolderService.tranferFundDestinationAccount(id, ammout);
                }
            } else if (studentcheckingDB.isPresent()) {
                if (passwordEncoder.matches(acKey, studentcheckingDB.get().getSecretKey()) && passwordEncoder.matches(hashkey,thirdPartyDB.getPassword())) {
                    accountHolderService.tranferFundDestinationAccount(id, ammout);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ID doesn't belong to any account");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Thrid party does not exist");
        }
    }
}
