package com.scm.scm20.services.impl;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.helpers.ResourceNotFoundException;
import com.scm.scm20.repositories.ContactRepo;
import com.scm.scm20.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {
        
        String contactId = UUID.randomUUID().toString();

        contact.setId(contactId);

        contact.setCreatedAt(LocalDateTime.now());

        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        
        Contact contactOld = contactRepo.findById(contact.getId()).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setPicture(contact.getPicture());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id" + id));
    }

    @Override
    public void delete(String id) {
        var contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id" + id));
        contactRepo.delete(contact);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("descending") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int page, int size, String sortBy, String order, User user) {
        
        Sort sort = order.equals("descending") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByNameContainingAndUser(nameKeyword, user, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortBy,
            String order, User user) {

                Sort sort = order.equals("descending") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

                var pageable = PageRequest.of(page, size, sort);
                
                return contactRepo.findByPhoneNumberContainingAndUser(phoneNumberKeyword, user, pageable);
    }

    @Override
    public Page<Contact> searachByEmail(String emailKeyword, int page, int size, String sortBy, String order, User user) {
       
        Sort sort = order.equals("descending") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByEmailContainingAndUser(emailKeyword, user, pageable);
    }

    @Override
    public List<Contact> getContacts(User user) {
        // TODO Auto-generated method stub
        return contactRepo.findTop5ByUserOrderByCreatedAtDesc(user);
    }

}
