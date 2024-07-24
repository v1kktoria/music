package com.example.music.service;

import com.example.music.model.User;

public interface UserService {
    void saveUser(User user);
    User findByEmail(String email);
    boolean userExists(String email);
}
