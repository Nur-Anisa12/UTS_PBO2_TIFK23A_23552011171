package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Menampilkan form login
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // Akan load templates/login.html
    }

    @GetMapping("/auth/todos")
    public String todos() {
        return "todos"; // Akan mencari index.html di templates/
        // atau return "forward:/index.html" untuk file static
    }

    // Menampilkan form registrasi
    @GetMapping("/register")
    public String registerForm() {
        return "register"; // Akan load templates/register.html
    }

    // Menangani registrasi POST request
    @PostMapping("/register")
    public String register(User user, Model model) {
        // Debug log untuk melihat data input
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password (raw): " + user.getPassword());

        // Validasi jika username sudah ada
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username sudah terdaftar");
            return "register"; // Jika username sudah ada, kembali ke halaman registrasi dengan error message
        }

        // Enkripsi password sebelum disimpan
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Simpan data user baru
        userRepo.save(user);

        // Redirect ke halaman login setelah registrasi berhasil
        return "redirect:/login";
    }
}
