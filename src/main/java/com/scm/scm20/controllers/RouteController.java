package com.scm.scm20.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.helpers.Helper;
import com.scm.scm20.services.ContactService;
import com.scm.scm20.services.UserService;

@ControllerAdvice
public class RouteController {

    private Logger logger = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    UserService userService;

    @Autowired
    ContactService contactService;


    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){

        if(authentication == null){
            return;
        }

        System.out.print("Adding Logged in user info to the model");
        
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in" + username);

        User user = userService.getUserByEmail(username);

        System.out.println(user.getName());
        System.out.println(user.getEmail());

        model.addAttribute("LoggedInUser", user);

        List<Contact> contactList = contactService.getByUserId(user.getUserId());

        model.addAttribute("totalContacts", contactList.size());

        List<Contact> recentContactList = contactService.getContacts(user);

        model.addAttribute("recentContacts", recentContactList);

    }

}
