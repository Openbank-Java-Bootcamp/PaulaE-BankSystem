package com.ironhack.midterm.Controller.Implementation.Users;

import com.ironhack.midterm.Controller.Interface.Users.AdminControllerInterface;
import com.ironhack.midterm.DTO.UsersDTO.AdminDTO;
import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Service.Interface.Users.AdminServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController implements AdminControllerInterface {

    @Autowired
    AdminServiceInterface adminServiceInterface;

    @PostMapping("/admins")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAdmin(@RequestBody @Valid AdminDTO adminDTO){
        adminServiceInterface.saveAdmin(adminDTO);
    }

    @DeleteMapping("/admins/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteAdmin(@PathVariable int  id) {
        adminServiceInterface.deleteAdmin(id);
    }
}
