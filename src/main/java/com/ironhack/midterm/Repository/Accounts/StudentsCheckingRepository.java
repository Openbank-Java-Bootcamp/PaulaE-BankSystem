package com.ironhack.midterm.Repository.Accounts;

import com.ironhack.midterm.Model.Accounts.Checking;
import com.ironhack.midterm.Model.Accounts.Savings;
import com.ironhack.midterm.Model.Accounts.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface StudentsCheckingRepository extends JpaRepository<StudentChecking, Integer> {

    @Query(nativeQuery = true, value = "SELECT balance, balance_currency FROM student_checking WHERE id = :id")
    List<Object> findBalanceById(int id);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM student_checking WHERE primary_owner_id = :id OR secondary_owner_id = :id")
    int findNumberAccountsByUserid(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM student_checking JOIN account_holder ON student_checking.primary_owner_id = account_holder.user_id OR student_checking.secondary_owner_id = account_holder.user_id  WHERE account_holder.username = :username" )
    List<StudentChecking> findbyUserName(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM student_checking JOIN account_holder ON student_checking.primary_owner_id = account_holder.user_id " +
            "OR student_checking.secondary_owner_id = account_holder.user_id WHERE account_holder.username = :name AND account_holder.name = :username AND student_checking.balance > :amounth ")
    List<StudentChecking>  findBalanceByUserNameAndNameAndAmount(String name, String username, BigDecimal amounth);
}
