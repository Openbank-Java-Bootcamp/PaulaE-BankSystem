package com.ironhack.midterm.Model.Accounts;

import com.ironhack.midterm.Enum.Status;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Class.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;


@Entity
public class Savings extends Account {

    @NotNull
    private String secretKey;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(precision = 32, scale = 6)
    private BigDecimal interestRate;

    private Date lastInterestRate;
    private Date lastTransactionMade;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimumBalance")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimumBalance_Currency"))
    })
    private Money minimumBalance;

    public Savings() {
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal interestRate)  {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        //default value
        this.status = Status.ACTIVE;
        //default value
        this.minimumBalance = new Money(new BigDecimal(250));
        //interestRate between 0.0025 [default value] and 0.5
        BigDecimal maxInterestRate = new BigDecimal(0.5);
        BigDecimal minInterestRate = new BigDecimal(0.0025);
        if (interestRate == null){
            this.interestRate = minInterestRate;
        }else {
            if (maxInterestRate.compareTo(interestRate) == 1 && interestRate.compareTo(minInterestRate) == 1) {
                this.interestRate = interestRate;
            } else {
                this.interestRate = minInterestRate;
            }
        }
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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Date getLastInterestRate() {
        return lastInterestRate;
    }

    public void setLastInterestRate(Date lastInterestRate) {
        this.lastInterestRate = lastInterestRate;
    }

    //penalty fee application
    public void penaltyFee(){
        //this is the condition but i think here we dont need to applicate it
        //it does not go there, that condition must be ask every time a transation is made over the remind balance
        if (super.getBalance().getAmount().compareTo(minimumBalance.getAmount()) < 0) {
            super.setBalance(new Money(getBalance().decreaseAmount(super.getPenaltyFee())));
        }
    }

    //interest rate
    // When a savings account balance is accessed,
    // you must determine if it has been 1 year or more since either the account was created
    // or since interest was added to the account
    public void interestRate(){
        Date currentTime = new Date();
        double days = DaysSinceLastInterest(currentTime);
        if (days >= 365){
            super.getBalance().increaseAmount(super.getBalance().getAmount().multiply(interestRate));
        }
    }

    public double DaysSinceLastInterest(Date currentTime){
        long currentTimeLong = currentTime.getTime();
        long initialTime;
        if (lastInterestRate != null) {
            initialTime = lastInterestRate.getTime();
        }
        else{
            initialTime = super.getCreationDate().getTime();
            lastInterestRate = new Date();
        }
        long difference = currentTimeLong - initialTime;
        double days = Math.floor(difference / (1000 * 60 * 60 * 24));
        return days;
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

