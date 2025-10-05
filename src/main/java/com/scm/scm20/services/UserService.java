package com.scm.scm20.services;

import java.util.*;
import com.scm.scm20.entities.User;

public interface UserService {

    User  saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExists(String id);
    boolean isUserExistsByEmail(String email);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    Optional<User> getUserByEmailToken(String token);
    User  verifyUser(User user);

    // Add  more methods related to user
}
