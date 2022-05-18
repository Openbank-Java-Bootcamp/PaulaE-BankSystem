package com.ironhack.midterm.Controller.Implementation.Accounts;

import com.ironhack.midterm.Controller.Interface.Accounts.CreditCardControllerInterface;
import com.ironhack.midterm.DTO.AccountsDTO.BalanceOnlyDTO;
import com.ironhack.midterm.DTO.AccountsDTO.CreditCardDTO;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Service.Interface.Accounts.CreditCardServiceInterface;
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
public class CreditCardController implements CreditCardControllerInterface {

    @Autowired
    CreditCardServiceInterface creditCardServiceInterface;

    @PostMapping("/creditcards")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCreditCard(@RequestBody @Valid CreditCardDTO creditCardDTO) throws Exception {
        creditCardServiceInterface.saveCreditCard(creditCardDTO);
    }

    @GetMapping("/creditcards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getBalanceById(@PathVariable int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return creditCardServiceInterface.getBalanceById(id);
        }else{
            String AccountHolderUserName = auth.getName();
            if (creditCardServiceInterface.UserAccount(id,AccountHolderUserName)){
                return creditCardServiceInterface.getBalanceById(id);
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account is not yours");
            }
        }
    }





    @PatchMapping("/creditcards/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable int id, @RequestBody BalanceOnlyDTO balanceOnlyDTO){
        creditCardServiceInterface.updateBalance(id, balanceOnlyDTO.getBalance());
    }


    @DeleteMapping("/creditcards/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCreditCard(@PathVariable int id) {
       creditCardServiceInterface.deleteCreditCard(id);
    }
}
