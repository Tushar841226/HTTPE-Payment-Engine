package com.httpe.auth.service;

import com.httpe.auth.entity.User;
import com.httpe.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.httpe.auth.security.JwtUtil;
import java.util.List;

@Service
public class UserService {

    private final UserRepository UserRepository;

    private final JwtUtil jwtUtil;

    public UserService(UserRepository UserRepository, JwtUtil jwtUtil) {
        this.UserRepository = UserRepository;
        this.jwtUtil = jwtUtil;
    }

    public User saveUser(User user) {

    if (UserRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new RuntimeException("Email already exists");
    }

    return UserRepository.save(user);
    }

    public List<User> getAllUsers() {
        return UserRepository.findAll();
    }
    public String login(String email, String password) {

    User user = UserRepository.findByEmail(email).orElse(null);

    if (user != null && user.getPassword().equals(password)) {
        return jwtUtil.generateToken(user.getEmail());
    }

    throw new RuntimeException("Invalid Email or Password");
}
}
