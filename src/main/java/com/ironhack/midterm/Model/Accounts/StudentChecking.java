package com.ironhack.midterm.Model.Accounts;

import com.ironhack.midterm.Enum.Status;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Class.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class StudentChecking extends Account {

    @NotNull
    private String secretKey;


    @Enumerated(value = EnumType.STRING)
    private Status status;
    private Date lastTransactionMade;

    public StudentChecking() {
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
    }


    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean fraudDetection(){
        Date date = new Date();
        boolean notfraud = false;
        if (lastTransactionMade != null) {
            if(lastTransactionMade.getYear() == date.getYear() && lastTransactionMade.getMonth() == date.getMonth()
                    && lastTransactionMade.getDay() == date.getDay() && lastTransactionMade.getHours() == date.getHours()
                    && lastTransactionMade.getHours() == date.getHours() && lastTransactionMade.getMinutes() == date.getMinutes()
                    && (lastTransactionMade.getSeconds() + 30) > date.getSeconds() ){
                this.setStatus(Status.FROZEN);
                notfraud = true;
            }else {
                lastTransactionMade = date;
            }
        }else{
            lastTransactionMade = date;
        }
        return notfraud;
    }

    public Date getLastTransactionMade() {
        return lastTransactionMade;
    }

    public void setLastTransactionMade(Date lastTransactionMade) {
        this.lastTransactionMade = lastTransactionMade;
    }
}

