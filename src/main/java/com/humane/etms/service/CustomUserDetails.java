package com.humane.etms.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    @Getter
    private Collection<String> admissions;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Collection<String> admissions) {
        super(username, password, authorities);
        this.admissions = admissions;
    }
}
