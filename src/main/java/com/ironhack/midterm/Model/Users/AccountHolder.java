package com.ironhack.midterm.Model.Users;

import com.ironhack.midterm.Model.Class.Address;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Date;

@Entity
public class AccountHolder extends User {

    @NotNull
    private Date dateOfBrith;
    @NotNull
    @Embedded
    private Address primaryAddress;

    private String mailAddress;

    public AccountHolder() {
    }

    public AccountHolder(String name, Collection<Role> roles, Date dateOfBrith, Address primaryAddress, String mailAddress, String password, String username) {
        super(name, roles, password, username);
        this.dateOfBrith = dateOfBrith;
        this.primaryAddress = primaryAddress;
        this.mailAddress = mailAddress;
    }

    public Date getDateOfBrith() {
        return dateOfBrith;
    }

    public void setDateOfBrith(Date dateOfBrith) {
        this.dateOfBrith = dateOfBrith;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }


}

