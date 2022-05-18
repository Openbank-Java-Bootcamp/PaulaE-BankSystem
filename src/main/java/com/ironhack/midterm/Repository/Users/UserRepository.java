package com.ironhack.midterm.Repository.Users;

import com.ironhack.midterm.Model.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String name);
}
