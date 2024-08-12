package com.example.music.controller;

import com.example.music.model.User;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


public class AuthControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthController authController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setViewResolvers(new InternalResourceViewResolver())
                .build();
    }

    @Test
    void testSigninForm() throws Exception {
        mockMvc.perform(get("/auth/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testSignupForm() throws Exception {
        mockMvc.perform(get("/auth/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testSignout() throws Exception {
        mockMvc.perform(get("/auth/signout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/signin"));
    }

    @Test
    void testSignupSuccess() throws Exception {
        User user = new User();
        user.setEmail("user@gmail.com");
        Mockito.when(userService.userExists(Mockito.anyString())).thenReturn(false);
        Mockito.doNothing().when(userService).saveUser(Mockito.any(User.class));
        mockMvc.perform(post("/auth/signup")
                .param("email", "user@gmail.com")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/signin"));
        Mockito.verify(userService, Mockito.times(1)).saveUser(Mockito.any(User.class));
    }

    @Test
    void testSignupUserAlreadyExists() throws Exception {
        User user = new User();
        user.setEmail("user@gmail.com");
        Mockito.when(userService.userExists(Mockito.anyString())).thenReturn(true);
        mockMvc.perform(post("/auth/signup")
                .param("email", "user@gmail.com")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "User already exists with email user@gmail.com"));
        Mockito.verify(userService, Mockito.never()).saveUser(Mockito.any(User.class));
    }
}
