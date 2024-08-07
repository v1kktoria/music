package com.example.music.service;

import com.example.music.model.User;

import java.util.Set;

public interface UserService {
    void saveUser(User user);
    User findByEmail(String email);
    boolean userExists(String email);
    User findById(Long id) throws Exception;
    void updateUserProfile(Long id, String avatarUrl, String backgroundUrl, String username, String country) throws Exception;
    Set<User> getFollowers(Long id) throws Exception;
    Set<User> getFollowing(Long id) throws Exception;
    void follow(Long currentId, Long targetId) throws Exception;
    void removeFollow(Long currentId, Long targetId) throws Exception;
    boolean isFollow(Long currentId, Long targetId) throws Exception;

}
