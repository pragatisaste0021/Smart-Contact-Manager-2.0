package com.scm.scm20.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.forms.ContactForm;
import com.scm.scm20.forms.ContactSearchForm;
import com.scm.scm20.helpers.AppConstants;
import com.scm.scm20.helpers.Helper;
import com.scm.scm20.helpers.Message;
import com.scm.scm20.helpers.MessageType;
import com.scm.scm20.services.ContactService;
import com.scm.scm20.services.ImageService;
import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // add contact page

    @RequestMapping("/add")
    public String addContactView(Model model){

        ContactForm contactForm = new ContactForm();
        contactForm.setFavorite(true);

        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication, HttpSession session){

        // Process the form data

        // Validate Form

        if(result.hasErrors()){
            session.setAttribute("message", Message.builder().content("Please correct the following errors").type(MessageType.red).build());
            return "user/add_contact";
        }

        // Contact form -> contact

        String email = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(email);

        // Process the contact picture

        Contact contact = new Contact();

        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());


        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            // / Upload the file
         String filename = UUID.randomUUID().toString();
         String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
        // Set the contact picture URL
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(filename);
        }
        
        contactService.save(contact);

         // Set messages on the view
        session.setAttribute("message", Message.builder().content("You have successfully added a new contact").type(MessageType.green).build());

        return "redirect:/user/contacts/add";
    }

    // View Contacts

    @RequestMapping
    public String viewContacts(@RequestParam(value = "page",  defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size, @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "direction", defaultValue = "ascending") String direction, Model model, Authentication authentication){

        // Load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        model.addAttribute("totalContacts", pageContact.getTotalElements());

        return "user/contacts";
    }

    // Search Handler

    @RequestMapping("/search")
    public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size, @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "order", defaultValue = "ascending") String order, Model model, Authentication authentication){

        String field = contactSearchForm.getField();
        String value = contactSearchForm.getValue();

        logger.info("field {} keyword {}", field, value);

        String username = Helper.getEmailOfLoggedInUser(authentication);

        var user = userService.getUserByEmail(username);

        Page<Contact> pageContact = null;

        if(field.equalsIgnoreCase("name")){
            pageContact = contactService.searchByName(value, page, size, sortBy, order, user);
        }
        else if(field.equalsIgnoreCase("email")){
            pageContact = contactService.searachByEmail(value, page, size, sortBy, order, user);
        }
        else if(field.equalsIgnoreCase("phone")){
            pageContact = contactService.searchByPhoneNumber(value, page, size, sortBy, order, user);
        }

        logger.info("pageContact {}", pageContact);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/search";
    }

    // Delete Contact

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId, HttpSession session){
        contactService.delete(contactId);
        logger.info("contactId {} deleted", contactId);

        session.setAttribute("message", Message.builder().content("Contact is deleted successfully !!").type(MessageType.green).build());
        return "redirect:/user/contacts";
    }

    // Update Contact Form View

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable("contactId") String contactId, Model model){
        var contact = contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact_view";
    }

    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId, @Valid @ModelAttribute ContactForm contactForm, Model model, HttpSession session, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "/user/update_contact_view";
        }
        // Update the contact

        var contact = contactService.getById(contactId);

        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setPhoneNumber(contactForm.getPhoneNumber());

        // Process Image

        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){

            logger.info("File is not empty");

            String filename = UUID.randomUUID().toString();

            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), filename);

            System.out.println(imageUrl);

            contact.setCloudinaryImagePublicId(filename);
            contact.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        }
        else{
            logger.info("Picture {}", contactForm.getPicture());
            logger.info("File ie empty");
        }

        var updatedContact = contactService.update(contact);
        logger.info("Updated Contact {}", updatedContact);
        session.setAttribute("message", Message.builder().content("Contact Updated Successfully").type(MessageType.green).build());
        
        return "redirect:/user/contacts/view/"+contactId;
    }

}
