package com.scm.scm20.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.scm20.entities.ContactQuery;

@Repository
public interface ContactQueryRepo extends JpaRepository<ContactQuery, String>{

}
