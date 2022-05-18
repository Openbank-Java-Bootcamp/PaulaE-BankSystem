package com.ironhack.midterm.Model.Accounts;

import com.ironhack.midterm.Enum.Status;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Class.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Checking extends Account {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimumBalance")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimumBalance_Currency"))
    })
    private Money minimumBalance;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "monthlyMaintenanceFee")),
            @AttributeOverride(name = "currency", column = @Column(name = "monthlyMaintenanceFee_Currency"))
    })
    private Money monthlyMaintenanceFee;

    @NotNull
    private String secretKey;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private Date lastMaintenanceFee;

    public Checking() {
    }

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        //default value
        this.minimumBalance = new Money(new BigDecimal(250));
        //default value
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
        //initializes in the current system time
        //default value
        this.status = Status.ACTIVE;
    }

    //getters and setters

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
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

    //penalty fee application
    public void penaltyFee(){
        //this is the condition but i think here we dont need to applicate it
        //it does not go there, that condition must be ask every time a transation is made over the remind balance
        if (super.getBalance().getAmount().compareTo(minimumBalance.getAmount()) < 0) {
            super.setBalance(new Money(getBalance().decreaseAmount(super.getPenaltyFee())));
        }
    }

    public void interestMantenanceFee(){
        Date currentTime = new Date();
        double days = DaysSinceLastMaintenanceFee(currentTime);
        if (days >= 30){
            super.getBalance().decreaseAmount(monthlyMaintenanceFee.getAmount());
        }
    }

    public double DaysSinceLastMaintenanceFee(Date currentTime){
        long currentTimeLong = currentTime.getTime();
        long initialTime;
        if (lastMaintenanceFee != null) {
            initialTime = lastMaintenanceFee.getTime();
        }
        else{
            initialTime = super.getCreationDate().getTime();
            lastMaintenanceFee = new Date();
        }
        long difference = currentTimeLong - initialTime;
        double days = Math.floor(difference / (1000 * 60 * 60 * 24));
        return days;
    }

}
