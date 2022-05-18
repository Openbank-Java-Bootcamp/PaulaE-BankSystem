package com.ironhack.midterm.Controller.Implementation.Accounts;

import com.ironhack.midterm.Controller.Interface.Accounts.StudentCheckingControllerInterface;
import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.Model.Accounts.StudentChecking;
import com.ironhack.midterm.Service.Interface.Accounts.StudentsCheckingServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentCheckingController implements StudentCheckingControllerInterface {

    @Autowired
    StudentsCheckingServiceInterface studentsCheckingServiceInterface;

    @GetMapping("/studentcheckings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Object> getBalanceById(@PathVariable int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return studentsCheckingServiceInterface.getBalanceById(id);
        }else{
            String AccountHolderUserName = auth.getName();
            if (studentsCheckingServiceInterface.UserAccount(id,AccountHolderUserName)){
                return studentsCheckingServiceInterface.getBalanceById(id);
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account is not yours");
            }
        }
    }

    @PatchMapping("/studentcheckings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable int id, @RequestBody BalanceOnlyDTO balanceOnlyDTO){
        studentsCheckingServiceInterface.updateBalance(id, balanceOnlyDTO.getBalance());
    }

    @DeleteMapping("/studentcheckings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentChecking(@PathVariable int id) {
        studentsCheckingServiceInterface.deleteStudentChecking(id);
    }
}
