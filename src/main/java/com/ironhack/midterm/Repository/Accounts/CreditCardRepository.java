package com.ironhack.midterm.Repository.Accounts;

import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.CreditCard;
import com.ironhack.midterm.Model.Class.Money;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    @Query(nativeQuery = true, value = "SELECT balance, balance_currency FROM credit_card WHERE id = :id")
    List<Object> findBalanceById(int id);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM credit_card WHERE primary_owner_id = :id OR secondary_owner_id = :id")
    int findNumberAccountsByUserid(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM credit_card JOIN account_holder ON credit_card.primary_owner_id = account_holder.user_id OR credit_card.secondary_owner_id = account_holder.user_id  WHERE account_holder.username = :username" )
    List<CreditCard> findbyUserName(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM credit_card JOIN account_holder ON credit_card.primary_owner_id = account_holder.user_id " +
            "OR credit_card.secondary_owner_id = account_holder.user_id WHERE account_holder.username = :username AND account_holder.name = :name AND credit_card.balance > :amounth ")
    List<CreditCard>findBalanceByUserNameAndNameAndAmount(String name, String username, BigDecimal amounth);
}
