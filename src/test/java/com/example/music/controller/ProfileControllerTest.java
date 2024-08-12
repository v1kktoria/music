package com.example.music.controller;

import com.example.music.model.User;
import com.example.music.service.SongService;
import com.example.music.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ProfileControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private SongService songService;
    @InjectMocks
    private ProfileController profileController;
    private Principal principal;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        principal = Mockito.mock(Principal.class);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setViewResolvers(new InternalResourceViewResolver())
                .build();
    }

    @Test
    void testProfile() throws Exception {
        User user = new User();
        user.setId(1L);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(userService.isFollow(Mockito.anyLong(),Mockito.anyLong())).thenReturn(true);
        Mockito.when(songService.findSongsByUser(user)).thenReturn(Collections.emptySet());
        Mockito.when(userService.getFollowers(Mockito.anyLong())).thenReturn(Collections.emptySet());
        Mockito.when(userService.getFollowing(Mockito.anyLong())).thenReturn(Collections.emptySet());
        mockMvc.perform(get("/profile/1")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("isOwner", true))
                .andExpect(model().attribute("songs", Collections.emptySet()))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("followers", Collections.emptySet()))
                .andExpect(model().attribute("following", Collections.emptySet()))
                .andExpect(model().attribute("isFollow", true));
    }

    @Test
    void testUpdate() throws Exception {
        User user = new User();
        user.setId(1L);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.doNothing().when(userService).updateUserProfile(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        mockMvc.perform(post("/profile/update")
                .param("avatarUrl", "http://test.com/avatar.jpg")
                .param("backgroundUrl", "http://test.com/background.jpg")
                .param("username", "username")
                .param("country", "country")
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/1"));
        Mockito.verify(userService, Mockito.times(1)).updateUserProfile(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testFollow() throws Exception {
        User user = new User();
        user.setId(1L);
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.doNothing().when(userService).follow(Mockito.anyLong(), Mockito.anyLong());
        mockMvc.perform(post("/profile/follow/2")
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/2"));
        Mockito.verify(userService, Mockito.times(1)).follow(Mockito.anyLong(), Mockito.eq(2L));
    }

    @Test
    void testUnFollow() throws Exception {
        User user = new User();
        user.setId(1L);
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.doNothing().when(userService).removeFollow(Mockito.anyLong(), Mockito.anyLong());
        mockMvc.perform(post("/profile/unfollow/2")
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/2"));
        Mockito.verify(userService, Mockito.times(1)).removeFollow(Mockito.anyLong(), Mockito.eq(2L));
    }
}

