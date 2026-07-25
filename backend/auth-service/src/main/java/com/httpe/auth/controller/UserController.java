package com.httpe.auth.controller;

import com.httpe.auth.dto.LoginRequest;
import com.httpe.auth.dto.LoginResponse;
import com.httpe.auth.entity.User;
import com.httpe.auth.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService UserService;

    public UserController(UserService UserService) {
        this.UserService = UserService;
    }
    

    @PostMapping
    public User saveUser(@RequestBody User user) {
        return UserService.saveUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return UserService.getAllUsers();
    }
    
    @PostMapping("/login")
public LoginResponse login(@RequestBody LoginRequest request) {

    String token = UserService.login(
            request.getEmail(),
            request.getPassword()
    );

    return new LoginResponse(token);
}
}
