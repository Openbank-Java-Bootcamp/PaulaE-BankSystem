package com.ironhack.midterm.Repository.Users;

import com.ironhack.midterm.Model.Class.Address;
import com.ironhack.midterm.Model.Users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Integer> {

    Optional<AccountHolder> findByDateOfBrithAndPrimaryAddressAndName(Date dateOfBirth, Address primaryAddres, String name);
}
