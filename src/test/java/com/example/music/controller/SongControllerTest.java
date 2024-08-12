package com.example.music.controller;

import com.example.music.model.Song;
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


public class SongControllerTest {
    @Mock
    private SongService songService;
    @Mock
    private UserService userService;
    @InjectMocks
    private SongController songController;
    private MockMvc mockMvc;
    private Principal principal;

    @BeforeEach
    void setUp() {
        principal = Mockito.mock(Principal.class);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(songController)
                .setViewResolvers(new InternalResourceViewResolver())
                .build();
    }

    @Test
    void testIndex() throws Exception {
        User user = new User();
        user.setId(1L);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(songService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("songs", "userId", "user"));
    }

    @Test
    void testAddSongForm() throws Exception {
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addSong"));
    }

    @Test
    void testAddSong() throws Exception {
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        User user = new User();
        user.setId(1L);
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        mockMvc.perform(multipart("/addSong")
                        .file("audioFile", new byte[0])
                        .param("title", "Title")
                        .param("artist", "Artist")
                        .param("genre", "Genre")
                        .param("imageURL", "http://test.com/image.jpg")
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        Mockito.verify(songService, Mockito.times(1)).saveSong(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void testDeleteSong() throws Exception {
        User user = new User();
        user.setId(1L);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        mockMvc.perform(delete("/delete")
                        .principal(principal)
                        .param("songId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/1"));
        Mockito.verify(songService, Mockito.times(1)).deleteSong(Mockito.anyLong());
    }

    @Test
    void testEditSongForm() throws Exception {
        Song song = new Song();
        song.setId(1L);
        Mockito.when(songService.findSongById(Mockito.anyLong())).thenReturn(song);
        mockMvc.perform(get("/edit")
                        .param("songId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editSong"))
                .andExpect(model().attributeExists("song"));
    }

    @Test
    void testUpdateSong() throws Exception {
        User user = new User();
        user.setId(2L);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        mockMvc.perform(multipart("/edit")
                        .file("audioFile", new byte[0])
                        .param("id", "1")
                        .param("title", "Title")
                        .param("artist", "Artist")
                        .param("genre", "Genre")
                        .principal(principal)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/2"));
        Mockito.verify(songService, Mockito.times(1)).updateSong(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any());
    }

    @Test
    void testSearch() throws Exception {
        User user = new User();
        user.setId(1L);
        Mockito.when(principal.getName()).thenReturn("user@gmail.com");
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(songService.searchSongs(Mockito.anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/search")
                        .param("query", "searchQuery")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("songs", "userId", "user"));
    }
}
