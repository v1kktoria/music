package com.example.music.service;

import com.example.music.exception.UserNotFoundException;
import com.example.music.model.User;
import com.example.music.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User user;
    @BeforeEach
    void setUp()
    {
        user = new User();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser()
    {
        user.setPassword("password");
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        userService.saveUser(user);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("password");
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Assertions.assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testFindByEmail()
    {
        user.setEmail("test@gmail.com");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        User result = userService.findByEmail("test@gmail.com");
        Assertions.assertEquals(user, result);
    }

    @Test
    void testUserExists()
    {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
        boolean exists = userService.userExists("test@gmail.com");
        Assertions.assertTrue(exists);
    }

    @Test
    void testFindById()
    {
        user.setId(1L);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        User result = userService.findById(1L);
        Assertions.assertEquals(user, result);
    }

    @Test
    void testFindByIdNotFound()
    {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.findById(1L);
        });
        Assertions.assertEquals("User not found with id 1", exception.getMessage());
    }

    @Test
    void testUpdateUserProfile()
    {
        user.setId(1L);
        user.setAvatarURL("Avatar Url");
        user.setBackgroundUrl("Background Url");
        user.setUsername("Username");
        user.setCountry("Country");
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        userService.updateUserProfile(1L, "New Avatar Url", "New Background Url", "New Username", "New Country");
        Assertions.assertEquals("New Avatar Url", user.getAvatarURL());
        Assertions.assertEquals("New Background Url", user.getBackgroundUrl());
        Assertions.assertEquals("New Username", user.getUsername());
        Assertions.assertEquals("New Country", user.getCountry());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testGetFollowers()
    {
        Set<User> followers = new HashSet<>();
        User follower = new User();
        followers.add(follower);
        user.setFollowers(followers);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Set<User> result = userService.getFollowers(1L);
        Assertions.assertEquals(followers, result);
    }

    @Test
    void testGetFollowing()
    {
        Set<User> following = new HashSet<>();
        User followee = new User();
        following.add(followee);
        user.setFollowing(following);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Set<User> result = userService.getFollowing(1L);
        Assertions.assertEquals(following, result);
    }

    @Test
    void testFollow()
    {
        User currentUser = new User();
        User targetUser = new User();
        Set<User> following = new HashSet<>();
        currentUser.setFollowing(following);
        Set<User> followers = new HashSet<>();
        targetUser.setFollowers(followers);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(currentUser)).thenReturn(Optional.of(targetUser));
        userService.follow(1L, 2L);
        Assertions.assertTrue(currentUser.getFollowing().contains(targetUser));
        Assertions.assertTrue(targetUser.getFollowers().contains(currentUser));
        Mockito.verify(userRepository, Mockito.times(2)).save(Mockito.any(User.class));
    }

    @Test
    void testRemoveFollow()
    {
        User currentUser = new User();
        User targetUser = new User();
        Set<User> following = new HashSet<>();
        following.add(targetUser);
        currentUser.setFollowing(following);
        Set<User> followers = new HashSet<>();
        followers.add(currentUser);
        targetUser.setFollowers(followers);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(currentUser)).thenReturn(Optional.of(targetUser));
        userService.removeFollow(1L, 2L);
        Assertions.assertFalse(currentUser.getFollowing().contains(targetUser));
        Assertions.assertFalse(targetUser.getFollowers().contains(currentUser));
        Mockito.verify(userRepository, Mockito.times(2)).save(Mockito.any(User.class));
    }

    @Test
    void testIsFollow()
    {
        User currentUser = new User();
        User targetUser = new User();
        Set<User> following = new HashSet<>();
        following.add(targetUser);
        currentUser.setFollowing(following);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(currentUser)).thenReturn(Optional.of(targetUser));
        boolean isFollowing = userService.isFollow(1L, 2L);
        Assertions.assertTrue(isFollowing);
    }

    @Test
    void testIsNotFollow()
    {
        User currentUser = new User();
        User targetUser = new User();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(currentUser)).thenReturn(Optional.of(targetUser));
        boolean isFollowing = userService.isFollow(1L, 2L);
        Assertions.assertFalse(isFollowing);

    }
}
