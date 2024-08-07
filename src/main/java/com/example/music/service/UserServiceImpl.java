package com.example.music.service;

import com.example.music.exception.UserNotFoundException;
import com.example.music.model.User;
import com.example.music.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public void saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id " + id)
        );
    }

    @Override
    public void updateUserProfile(Long id, String avatarUrl, String backgroundUrl, String username, String country) {
        User user = findById(id);
        user.setAvatarURL(avatarUrl);
        user.setBackgroundUrl(backgroundUrl);
        user.setUsername(username);
        user.setCountry(country);
        userRepository.save(user);
    }

    @Override
    public Set<User> getFollowers(Long id) {
        User user = findById(id);
        return user.getFollowers();
    }

    @Override
    public Set<User> getFollowing(Long id) {
        User user = findById(id);
        return user.getFollowing();
    }

    @Override
    public void follow(Long currentId, Long targetId) {
        User currentUser = findById(currentId);
        User targetUser = findById(targetId);
        if (!currentUser.getFollowing().contains(targetUser)) {
            currentUser.getFollowing().add(targetUser);
            targetUser.getFollowers().add(currentUser);
            userRepository.save(currentUser);
            userRepository.save(targetUser);
        }
    }

    @Override
    public void removeFollow(Long currentId, Long targetId) {
        User currentUser = findById(currentId);
        User targetUser = findById(targetId);
        if (currentUser.getFollowing().contains(targetUser)){
            currentUser.getFollowing().remove(targetUser);
            targetUser.getFollowers().remove(currentUser);
            userRepository.save(currentUser);
            userRepository.save(targetUser);
        }
    }

    @Override
    public boolean isFollow(Long currentId, Long targetId) {
        User currentUser = findById(currentId);
        User targetUser = findById(targetId);
        return currentUser.getFollowing().contains(targetUser);
    }
}
