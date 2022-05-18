package com.ironhack.midterm.Model.Accounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ironhack.midterm.Model.Users.AccountHolder;
import com.ironhack.midterm.Model.Class.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_Currency"))
    })
    private Money balance;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    private AccountHolder primaryOwner;
    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder SecondaryOwner;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "penaltyFee")),
            @AttributeOverride(name = "currency", column = @Column(name = "penaltyFee_Currency"))
    })
    private Money penaltyFee;

    private Date creationDate;

    public Account() {
    }

    //quitar el throw exception
    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner)  {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        SecondaryOwner = secondaryOwner;
        //default value is 40
        this.penaltyFee = new Money(new BigDecimal(40));
        this.creationDate = new Date();
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return SecondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        SecondaryOwner = secondaryOwner;
    }

    public Money getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(Money penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
