package com.scm.scm20.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.scm20.entities.User;
import java.util.*;

public interface UserRepo extends JpaRepository<User, String>{

    // extra methods related to db operations
    // Custom query methods
    // Custom finder methods

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmailToken(String token);
}
