package com.ironhack.midterm.Model.Accounts;

import com.ironhack.midterm.Enum.Status;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Class.Money;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
public class CreditCard extends Account {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "creditLimit")),
            @AttributeOverride(name = "currency", column = @Column(name = "creditLimit_Currency"))
    })
    private Money creditLimit;

    private BigDecimal interestRate;

    private Date lastInterestRate;


    public CreditCard() {
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        BigDecimal mimCreditLimit = new BigDecimal(100);
        BigDecimal maxCreditLimit = new BigDecimal(100000);
        if(creditLimit == null){
            this.creditLimit = new Money(mimCreditLimit);
        }else {
            if (maxCreditLimit.compareTo(creditLimit.getAmount()) == 1 && creditLimit.getAmount().compareTo(mimCreditLimit) == 1) {
                this.creditLimit = creditLimit;
            } else {
                this.creditLimit = new Money(mimCreditLimit);
            }
        }
        BigDecimal maxInterestRate = new BigDecimal(0.2);
        BigDecimal mimInterestRate = new BigDecimal(0.1);
        if (interestRate == null){
            this.interestRate = maxInterestRate;
        }else {
            if (maxInterestRate.compareTo(interestRate) == 1 && interestRate.compareTo(mimInterestRate) == 1) {
                this.interestRate = interestRate;
            } else {
                this.interestRate = maxInterestRate;
            }
        }

    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public void interestRate(){
        Date currentTime = new Date();
        double days = DaysSinceLastInterest(currentTime);
        if (days >= 30){
            super.getBalance().increaseAmount(super.getBalance().getAmount().multiply(interestRate.divide(new BigDecimal(12), 8, RoundingMode.DOWN)));
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


    public Date getLastInterestRate() {
        return lastInterestRate;
    }

    public void setLastInterestRate(Date lastInterestRate) {
        this.lastInterestRate = lastInterestRate;
    }
}

