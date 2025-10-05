package com.scm.scm20.repositories;

// import org.apache.http.annotation.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String>{

    // Find contact by user

    // Custom Finder Method
    Page<Contact> findByUser(User user, Pageable pageable);
    // Custom Query method
    
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByNameContainingAndUser(String nameKeyword, User user, Pageable pageable);

    Page<Contact> findByEmailContainingAndUser(String emailKeyword, User user, Pageable pageable);

    Page<Contact> findByPhoneNumberContainingAndUser(String phoneKeyword, User user, Pageable pageable);

    List<Contact> findTop5ByUserOrderByCreatedAtDesc(User user);


}
