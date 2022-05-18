package com.ironhack.midterm.Controller.Implementation.Users;

import com.ironhack.midterm.Controller.Interface.Users.ThirdPartyControllerInterface;
import com.ironhack.midterm.DTO.UsersDTO.ThridPartyDTO;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Repository.Users.AdminRepository;
import com.ironhack.midterm.Service.Interface.Users.AdminServiceInterface;
import com.ironhack.midterm.Service.Interface.Users.ThirdPartyServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteThirdParty(@PathVariable int  id) {
        thirdPartyServiceInterface.deleteThridParty(id);
    }
}
