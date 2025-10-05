package com.scm.scm20.helpers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Helper {

    // @Value("${server.baseUrl:http://localhost:8081}")
    // private String baseUrl;

    public static String getEmailOfLoggedInUser(Authentication authentication){

       if(authentication instanceof OAuth2AuthenticationToken){
        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        String username="";

        // Email/password

        OAuth2User oauthUser = (OAuth2User)authentication.getPrincipal();
        

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
            username = oauthUser.getAttribute("email").toString();
            System.out.println("Getting data from google");
        }
        else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
            System.out.println("Getting data from github");
            username = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString() + "@gmail.com";

        }
        return username;

       }

       else{
        System.out.println("Getting from local database");
        System.out.println(authentication.getName());
        return authentication.getName();
    }

    }


    public String getLinkForEmailVerification(String emailToken){

        // System.out.println(baseUrl);

        // String link = "http://localhost:8081/auth/verify-email?token=" + emailToken;

        System.out.println(emailToken);

        String encodedToken = URLEncoder.encode(emailToken, StandardCharsets.UTF_8);

        String link = "http://myweb10.ap-south-1.elasticbeanstalk.com/auth/verify-email?token=" + encodedToken;
                        

        return link;
    }

}
