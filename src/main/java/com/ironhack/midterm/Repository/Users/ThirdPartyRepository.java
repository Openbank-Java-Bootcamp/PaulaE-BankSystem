package com.ironhack.midterm.Repository.Users;

import com.ironhack.midterm.Model.Users.ThirdParty;
import com.ironhack.midterm.Model.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Integer> {

    Optional<ThirdParty> findByName(String name);
    ThirdParty findByUsername(String name);
}
