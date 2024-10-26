package com.excelr.service;

import com.excelr.model.User;
import com.excelr.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

 
}

