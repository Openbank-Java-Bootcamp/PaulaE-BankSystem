package com.ironhack.midterm.Repository.Accounts;

import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Class.Money;
import com.ironhack.midterm.Model.Users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Integer> {


    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM checking WHERE primary_owner_id = :id OR secondary_owner_id = :id")
    int findNumberAccountsByUserid(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM checking JOIN account_holder ON checking.primary_owner_id = account_holder.user_id OR checking.secondary_owner_id = account_holder.user_id  WHERE account_holder.username = :username" )
    List<Checking> findbyUserName(String username);

    //hemos cambiado username por name en el querie orden (username, name, quantity)
    @Query(nativeQuery = true, value = "SELECT * FROM checking JOIN account_holder ON checking.primary_owner_id = account_holder.user_id " +
            "OR checking.secondary_owner_id = account_holder.user_id WHERE account_holder.username = :name AND account_holder.name = :username AND checking.balance > :amounth ")
    List<Checking> findBalanceByUserNameAndNameAndAmount(String name, String username, BigDecimal amounth);


}
