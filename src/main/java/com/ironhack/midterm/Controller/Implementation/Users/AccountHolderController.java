package com.ironhack.midterm.Controller.Implementation.Users;

import com.ironhack.midterm.Controller.Interface.Users.AccountHolderControllerInterface;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Service.Interface.Users.AccountHolderServiceInterface;
import com.ironhack.midterm.Service.Interface.Users.ThirdPartyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class AccountHolderController implements AccountHolderControllerInterface {

    @Autowired
    ThirdPartyServiceInterface thirdPartyServiceInterface;
    @Autowired
    AccountHolderServiceInterface accountHolderServiceInterface;
    //accountHolder is created when an account is created no post method need.
    @PatchMapping("/transferfunds")
    @ResponseStatus(HttpStatus.OK)
    public void transferfunds(@RequestParam String username, @RequestParam int id, @RequestParam BigDecimal quantity){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String AccountHolderUserName = auth.getName();
        //con el username(login), name (primary or secondary owner) y el amount buscamos una cuenta que cumpla las características
        if (accountHolderServiceInterface.OriginAccountForTransferFund(AccountHolderUserName, username, quantity) && accountHolderServiceInterface.accountIdExist(id)){
            accountHolderServiceInterface.tranferFundDestinationAccount(id, quantity);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account fund to process the transaction");
        }
    }

    @PatchMapping("/transferfunds/thridparty")
    @ResponseStatus(HttpStatus.OK)
    public void transferfundsTP(@RequestParam String name, @RequestParam String TPname, @RequestParam BigDecimal quantity){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String AccountHolderUserName = auth.getName();
        if (accountHolderServiceInterface.OriginAccountForTransferFund(AccountHolderUserName, name, quantity) && thirdPartyServiceInterface.getByName(name)){
            accountHolderServiceInterface.tranferFundTP(TPname, quantity);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account fund to process the transaction");
        }
    }

}
