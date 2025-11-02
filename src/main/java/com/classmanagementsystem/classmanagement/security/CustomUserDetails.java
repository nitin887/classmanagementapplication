package com.classmanagementsystem.classmanagement.security;

import com.classmanagementsystem.classmanagement.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // Or user.getEmail() if you prefer login by email
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement logic for account expiration here
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can implement logic for account locking here
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can implement logic for credential expiration here
    }

    @Override
    public boolean isEnabled() {
        return true; // You can implement logic for account enabling/disabling here
    }
}
