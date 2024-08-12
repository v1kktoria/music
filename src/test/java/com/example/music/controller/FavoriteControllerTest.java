package com.example.music.controller;

import com.example.music.model.Song;
import com.example.music.model.User;
import com.example.music.service.FavoriteService;
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
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class FavoriteControllerTest {
    @Mock
    private FavoriteService favoriteService;
    @Mock
    private UserService userService;
    @Mock
    private SongService songService;
    @InjectMocks
    private FavoriteController favoriteController;
    private MockMvc mockMvc;
    private Principal principal;
    private User user;
    private Song song;
    @BeforeEach
    void setUp() {
        principal = Mockito.mock(Principal.class);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(favoriteController)
                .setViewResolvers(new InternalResourceViewResolver())
                .build();
        user = new User();
        song = new Song();
        user.setId(1L);
        song.setId(1L);
    }

    @Test
    void testAddFavorite() throws Exception {
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(songService.findSongById(Mockito.anyLong())).thenReturn(song);
        Mockito.doNothing().when(favoriteService).addFavorite(Mockito.any(User.class), Mockito.any(Song.class));
        mockMvc.perform(post("/favorite/add")
                .param("songId", "1")
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        Mockito.verify(favoriteService, Mockito.times(1)).addFavorite(Mockito.eq(user), Mockito.eq(song));
    }

    @Test
    void testRemoveFavorite() throws Exception {
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(songService.findSongById(Mockito.anyLong())).thenReturn(song);
        Mockito.doNothing().when(favoriteService).removeFavorite(Mockito.any(User.class), Mockito.any(Song.class));
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        mockMvc.perform(delete("/favorite/delete")
                .param("songId", "1")
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/favorite"));
        Mockito.verify(favoriteService, Mockito.times(1)).removeFavorite(Mockito.eq(user), Mockito.eq(song));
    }

    @Test
    void testFavorites() throws Exception {
        Set<Song> favorites = Collections.singleton(song);
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(favoriteService.getFavoriteSongs(Mockito.any(User.class))).thenReturn(favorites);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        mockMvc.perform(get("/favorite")
                .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("favorites"))
                .andExpect(model().attribute("favorites", favorites));
    }



}
