package com.scm.scm20.controllers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

// import java.lang.module.ModuleDescriptor.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm20.entities.User;
import com.scm.scm20.helpers.Message;
import com.scm.scm20.helpers.MessageType;
import com.scm.scm20.repositories.UserRepo;
import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam(value = "token", required=false) String token, HttpSession session){

        // byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedToken);
        // String token = new String(decodedBytes, StandardCharsets.UTF_8);

        token = URLDecoder.decode(token, StandardCharsets.UTF_8);

        User user = userService.getUserByEmailToken(token).orElse(null);

        System.out.println("User  is      "   +   user + "   Token is      "   +    token);

        if(user != null){
            // User is fetched

            System.out.println(user.getEmailToken() + " " + token);

            if(user.getEmailToken().equals(token)){
                user.setEmailVerified(true);
                user.setEnabled(true);

                userService.verifyUser(user);

                System.out.println("Verifed Successfully");

                session.setAttribute("message", Message.builder().content("Email Verified Successfully! Now You can login.").type(MessageType.green).build());

                return "success_page";
            }

            session.setAttribute("message", Message.builder().content("Email Not Verified! Token is not associated with the user").type(MessageType.red).build());
            return "error_page";
        }

        session.setAttribute("message", Message.builder().content("Email Not Verified! Token is not associated with the user please").type(MessageType.red).build());

        return "error_page";
    }
}
