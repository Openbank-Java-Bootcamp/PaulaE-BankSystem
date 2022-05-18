package com.ironhack.midterm.Controller.Implementation.Users;

import com.ironhack.midterm.Controller.Interface.Users.ThirdPartyControllerInterface;
import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Repository.Users.AdminRepository;
import com.ironhack.midterm.Service.Interface.Users.AccountHolderServiceInterface;
import com.ironhack.midterm.Service.Interface.Users.AdminServiceInterface;
import com.ironhack.midterm.Service.Interface.Users.ThirdPartyServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class ThirdPartyController implements ThirdPartyControllerInterface {

    @Autowired
    ThirdPartyServiceInterface thirdPartyServiceInterface;


    @PostMapping("/thirdparties")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveThridParty(@RequestBody @Valid ThridPartyDTO thirdParty) {
        thirdPartyServiceInterface.saveThridParty(thirdParty);
    }

    @DeleteMapping("/thirdparties/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteThirdParty(@PathVariable int  id) {
        thirdPartyServiceInterface.deleteThridParty(id);
    }

    @PatchMapping("/thridparty/transferfund")
    @ResponseStatus(HttpStatus.OK)
    public void sendMoney(@RequestParam String haskey, @RequestParam String secretkey,@RequestParam int id, @RequestParam BigDecimal amouth){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String TPUserName = auth.getName();
        thirdPartyServiceInterface.sendMoney(id, haskey, TPUserName,secretkey,amouth);
    }
}
