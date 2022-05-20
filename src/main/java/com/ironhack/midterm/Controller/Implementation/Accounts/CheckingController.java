package com.ironhack.midterm.Controller.Implementation.Accounts;

import com.ironhack.midterm.Controller.Interface.Accounts.CheckingControllerInterface;
import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.CheckingDTO;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Service.Interface.Accounts.CheckingServiceInterface;
import com.ironhack.midterm.Service.Interface.Accounts.StudentsCheckingServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CheckingController implements CheckingControllerInterface {


    @Autowired
    CheckingServiceInterface checkingService;

    @Autowired
    StudentsCheckingServiceInterface studentsCheckingServiceInterface;

    //private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @PostMapping("/checkings")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveChecking(@RequestBody @Valid CheckingDTO checking) throws Exception{
        int accountHolderYearBirth;
        try {
            accountHolderYearBirth =
                    new SimpleDateFormat("yyyy-MM-dd").parse(checking.getPrimaryOwner().getDateOfBirth()).getYear();
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The date of birth is not correct try: yyyy-MM-dd");
            }
        int todayYear = new Date().getYear();
        if (todayYear - accountHolderYearBirth < 24){
            studentsCheckingServiceInterface.saveStudentChecking(checking);
        }else {
            checkingService.saveChecking(checking);
        }
    }

    @GetMapping("/checkings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getBalanceById(@PathVariable int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return checkingService.getBalanceById(id);
        }else{
            String AccountHolderUserName = auth.getName();
            if (checkingService.UserAccount(id,AccountHolderUserName)){
                return checkingService.getBalanceById(id);
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account is not yours");
            }
        }
    }

    @PatchMapping("/checkings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable int id, @RequestBody @Valid BalanceOnlyDTO balanceOnlyDTO){
        checkingService.updateBalance(id, balanceOnlyDTO);
    }

    @DeleteMapping("/checkings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChecking(@PathVariable int id) {
        checkingService.deleteChecking(id);
    }

}
