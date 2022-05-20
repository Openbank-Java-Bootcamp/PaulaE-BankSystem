package com.ironhack.midterm.Repository.Accounts;

import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.CreditCard;
import com.ironhack.midterm.Model.Accounts.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface SavingsRepository extends JpaRepository<Savings, Integer> {

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM savings WHERE primary_owner_id = :id OR secondary_owner_id = :id")
    int findNumberAccountsByUserid(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM savings JOIN account_holder ON savings.primary_owner_id = account_holder.user_id OR savings.secondary_owner_id = account_holder.user_id  WHERE account_holder.username = :username" )
    List<Savings> findbyUserName(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM savings JOIN account_holder ON savings.primary_owner_id = account_holder.user_id " +
            "OR savings.secondary_owner_id = account_holder.user_id WHERE account_holder.username = :name AND account_holder.name = :username AND savings.balance > :amounth ")
    List<Savings> findBalanceByUserNameAndNameAndAmount(String name, String username, BigDecimal amounth);
}
