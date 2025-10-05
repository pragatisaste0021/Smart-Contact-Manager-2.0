package com.scm.scm20.services.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.scm20.entities.User;
import com.scm.scm20.helpers.AppConstants;
import com.scm.scm20.helpers.Helper;
import com.scm.scm20.helpers.ResourceNotFoundException;
import com.scm.scm20.repositories.UserRepo;
import com.scm.scm20.services.EmailService;
import com.scm.scm20.services.UserService;

import jakarta.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    Helper helper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {

        // User id : Have to generate
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        // Encode Password

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set the user role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        String emailToken = UUID.randomUUID().toString();

        user.setEmailToken(emailToken);

        User savedUser = userRepo.save(user);

        // User savedUser = user;

        String emailLink = helper.getLinkForEmailVerification(emailToken);

        // logger.info(emailLink);

        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart Contact Manager", emailLink);

        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 =  userRepo.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        // Save the user to database
        User save = userRepo.save(user2);

        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user =  userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        // user.getRoleList().clear();
        userRepo.delete(user);
    }

    @Override
    public boolean isUserExists(String id) {
        User user =  userRepo.findById(id).orElse(null);
        return user != null ? true : false;
        
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        User user =  userRepo.findByEmail(email).orElse(null);
        return user != null ? true :false;
    }

    @Override
    public List<User> getAllUsers() {
       return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<User> getUserByEmailToken(String token) {
        
        return Optional.ofNullable(userRepo.findByEmailToken(token).orElse(null));
    }

    @Override
    public User verifyUser(User user) {
        
        return userRepo.save(user);
    }

}
