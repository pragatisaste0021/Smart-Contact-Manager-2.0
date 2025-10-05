package com.scm.scm20.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.scm20.entities.ContactQuery;
import com.scm.scm20.repositories.ContactQueryRepo;
import com.scm.scm20.services.ContactQueryService;

@Service
public class ContactQueryServiceImpl implements ContactQueryService{

    @Autowired
    private ContactQueryRepo contactQueryRepo;

    @Override
    public ContactQuery save(ContactQuery contactQuery) {
        String contactQueryId = UUID.randomUUID().toString();

        contactQuery.setQId(contactQueryId);

        System.out.println("Contact Query Id Stored");

        return contactQueryRepo.save(contactQuery);
    }


}
