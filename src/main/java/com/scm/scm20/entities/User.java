package com.scm.scm20.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.Cascade;
import org.hibernate.engine.internal.ForeignKeys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(name = "user")
@Table(name = "users")
public class User implements UserDetails{

    @Id
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Getter(value = AccessLevel.NONE)
    private String password;

    @Lob
    @Column(length = 10000)
    private String about;

    @Lob
    @Column(length = 10000)
    private String profilePic;
    private String phoneNumber;

    @Getter(value = AccessLevel.NONE)
    private boolean enabled = false;

    private boolean emailVerified = false;
    private boolean phoneVerified;

    @Enumerated(EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerUserId;


    // Add more fields if needed

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)

    @CollectionTable(
    name = "user_role_list",
    joinColumns = @JoinColumn(name = "user_user_id")
)
    private List<String> roleList = new ArrayList<>();

    private String emailToken;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // List of roles 
        // Collection of simple authority
        Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    // For this project:
    // email : username

    @Override
    public String getUsername() {
       return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
     }
  
     @Override
     public boolean isAccountNonLocked() {
        return true;
     }
  
     @Override
     public boolean isCredentialsNonExpired() {
        return true;
     }
  
     @Override
     public boolean isEnabled() {
        return this.enabled;
     }

    @Override
    public String getPassword() {
        return this.password;
    }
    
 }
