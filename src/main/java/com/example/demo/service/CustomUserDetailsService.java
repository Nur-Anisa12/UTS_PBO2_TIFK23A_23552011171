package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cari user berdasarkan username dari database
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan: " + username));

        // Return Spring Security user object
        return org.springframework.security.core.userdetails.User.builder()

                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList()) // jika tidak menggunakan role
                .build();
    }
}
