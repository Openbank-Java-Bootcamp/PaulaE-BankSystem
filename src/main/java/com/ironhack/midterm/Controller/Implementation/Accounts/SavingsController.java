package com.ironhack.midterm.Controller.Implementation.Accounts;

import com.ironhack.midterm.Controller.Interface.Accounts.SavingsControllerInterface;
import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.SavingDTO;
import com.ironhack.midterm.Service.Interface.Accounts.SavingServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SavingsController implements SavingsControllerInterface {

    @Autowired
    SavingServiceInterface savingServiceInterface;

    @PostMapping("/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSaving(@RequestBody @Valid SavingDTO savingDTO) throws Exception{
        savingServiceInterface.saveSaving(savingDTO);
    }

    @GetMapping("/savings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Object> getBalanceById(@PathVariable int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return savingServiceInterface.getBalanceById(id);
        }else{
            String AccountHolderUserName = auth.getName();
            if (savingServiceInterface.UserAccount(id,AccountHolderUserName)){
                return savingServiceInterface.getBalanceById(id);
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account is not yours");
            }
        }
    }

    @PatchMapping("/savings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable int id, @RequestBody BalanceOnlyDTO balanceOnlyDTO){
        savingServiceInterface.updateBalance(id, balanceOnlyDTO.getBalance());
    }

    @DeleteMapping("/savings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSavings(@PathVariable int id) {
        savingServiceInterface.deleteSavings(id);
    }
}
