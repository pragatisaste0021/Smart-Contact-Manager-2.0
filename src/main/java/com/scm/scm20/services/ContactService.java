package com.scm.scm20.services;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;

import java.util.*;

import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

public interface ContactService {

    // Save contacts
    Contact save(Contact contact);

    // Update Contact
    Contact update(Contact contact);

    // Get Contacts
    List<Contact> getAll();

    // Get contact by id
    Contact getById(String id);

    // Delete Contact
    void delete(String id);

    // Search Contact
    Page<Contact> searchByName(String nameKeyword, int page, int size, String sortBy, String order, User user);

    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortBy, String order, User user);

    Page<Contact> searachByEmail(String emailKeyword, int page, int size, String sortBy, String order, User user);

    // get contacts by userid
    List<Contact> getByUserId(String userId);

    // Get contacts by user
    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);

    List<Contact> getContacts(User user);

}
