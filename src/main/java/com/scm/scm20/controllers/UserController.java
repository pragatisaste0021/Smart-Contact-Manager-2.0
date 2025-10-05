package com.scm.scm20.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scm20.entities.User;
import com.scm.scm20.helpers.Helper;
import com.scm.scm20.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService service;

    // User Dashboard Page
    @RequestMapping(value = "/dashboard")
    public String userDashboard(Authentication authentication) {
        
        return "user/dashboard";
    }

    // User Pofile
    @RequestMapping(value = "/profile")
    public String userProfile(Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in" + username);

        User user = service.getUserByEmail(username);

        System.out.println(user.getName());
        System.out.println(user.getEmail());

        model.addAttribute("LoggedInUser", user);

        return "user/profile";
    }
    

    // User add contact

    // User view Contact
    // Edit 
    // Delete
    // Search


}
