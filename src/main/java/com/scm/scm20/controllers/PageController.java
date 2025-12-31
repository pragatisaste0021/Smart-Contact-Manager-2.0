package com.scm.scm20.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.scm20.entities.ContactQuery;
import com.scm.scm20.entities.User;
import com.scm.scm20.forms.ContactQueryForm;
import com.scm.scm20.forms.UserForm;
import com.scm.scm20.helpers.Message;
import com.scm.scm20.helpers.MessageType;
// import com.scm.scm20.repositories.ContactQueryRepo;
import com.scm.scm20.services.ContactQueryService;
import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactQueryService contactQueryService;

    @GetMapping("/")
    public String index(){
        return "home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home Page Handler");

        // Sending data to the view
        model.addAttribute("name", "Substring Technologies");
        model.addAttribute("youTubeChannel", "DSA Algorithms");
        model.addAttribute("githubRepo", "https://github.com");
        return "home";
    }


    // About route

    @RequestMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("appName", "Smart Contact Manager");
        model.addAttribute("appDescription", "An efficient solution to manage your contacts securely.");
        model.addAttribute("companyName", "Smart Solutions Inc.");
        model.addAttribute("founders", "Samuel King, Grace Wilson");
        model.addAttribute("year", "1900");
        return "about"; 
    }

    // About Services

    @RequestMapping("/services")
    public String servicesPage(Model model){
        model.addAttribute("serviceName", "Smart Contact Manager");
        model.addAttribute("services", new String[]{
                "Contact Management",
                "Smart Search",
                "Automated Reminders",
                "Analytics and Reporting",
                "Cloud Sync"});
        return "services";
    }

    // About Contact

    @GetMapping("/contact")
    public String contact(Model model){
        model.addAttribute("contactQueryForm", new ContactQueryForm());
        return "contact"; 
    }

    @PostMapping("/submitContact")
    public String submitContact(@ModelAttribute ContactQueryForm contactQueryForm, Model model, HttpSession session) {
        // Save the form data to the database

        ContactQuery contactQuery = new ContactQuery();

        contactQuery.setName(contactQueryForm.getName());
        contactQuery.setEmail(contactQueryForm.getEmail());
        contactQuery.setMessage(contactQueryForm.getMessage());

        contactQueryService.save(contactQuery);

        // Add a success message to be displayed on the page
        Message message = Message.builder().content("Thank you! Your message has been received successfully.").type(MessageType.blue).build();

        session.setAttribute("message", message);

        // Return the contact page again, showing the success message
        return "contact";
    }

    // Login
    // login Controller - view
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // Register
    // Registrtion Page
    @GetMapping("/register")
    public String Register(Model model){
        UserForm userForm = new UserForm();
        // userForm.setName("Pragati");
        // userForm.setAbout("This is about : Write something");
        model.addAttribute("userForm", userForm);
        return "register";
    }

    // Processing Registration

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){

        // Fetch form data
        // Validate
        // Save
        // Message : successful
        // Redirect to register page

        System.out.print(userForm);

        if(rBindingResult.hasErrors()){
            return "register";
        }

        // Save to database
        // User service

        // User user = User.builder().name(userForm.getName()).email(userForm.getEmail()).password(userForm.getPassword())
        // .about(userForm.getAbout()).phoneNumber(userForm.getPhoneNumber()).profilePic("https://www.bing.com/ck/a?!&&p=115cad8fc84ea92eJmltdHM9MTcyOTY0MTYwMCZpZ3VpZD0zODQ5ZGQwYS01NDJlLTYyNTktMzk3MC1jYzM3NTU5YzYzYjgmaW5zaWQ9NTgwOQ&ptn=3&ver=2&hsh=3&fclid=3849dd0a-542e-6259-3970-cc37559c63b8&u=a1L2ltYWdlcy9zZWFyY2g_cT1kZWZhdWx0JTIwcHJvZmlsZSUyMHBpY3R1cmUmRk9STT1JUUZSQkEmaWQ9Qjk4MjIwQ0I5NUY2QzFFRTI2OEIzNjlCRkZEMUQ2QjhCQTc0NkFBMw&ntb=1").build();
        // System.out.print(user.getProvider());

        User user = new User();

        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://th.bing.com/th?id=OIP.CG70mC-flvJIYFRVmR9FZwHaHa&w=250&h=250&c=8&rs=1&qlt=90&o=6&dpr=1.4&pid=3.1&rm=2");
        user.setEnabled(false);

        User savedUser = userService.saveUser(user);

        Message message = Message.builder().content("Registration Successful").type(MessageType.blue).build();

        session.setAttribute("message", message);

        return "redirect:/register";
    }
    
}
