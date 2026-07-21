package com.httpe.auth.service;

import com.httpe.auth.entity.User;
import com.httpe.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository UserRepository;

    public UserService(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    public User saveUser(User user) {
        return UserRepository.save(user);
    }

    public List<User> getAllUsers() {
        return UserRepository.findAll();
    }
}