package com.example.music.service;

import com.example.music.model.Song;
import com.example.music.model.User;
import com.example.music.repository.SongRepository;
import com.example.music.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

public class FavoriteServiceImplTest {
    @InjectMocks
    private FavoriteServiceImpl favoriteService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SongRepository songRepository;
    private User user;
    private Song song;
    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setFavorites(new HashSet<>());
        song = new Song();
        song.setLikes(0);
    }

    @Test
    void testAddFavorite()
    {
        favoriteService.addFavorite(user, song);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(songRepository, Mockito.times(1)).save(song);
        Assertions.assertTrue(user.getFavorites().contains(song));
        Assertions.assertEquals(1, song.getLikes());
    }

    @Test
    void testRemoveFavorite()
    {
        user.getFavorites().add(song);
        song.setLikes(1);
        favoriteService.removeFavorite(user, song);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(songRepository, Mockito.times(1)).save(song);
        Assertions.assertFalse(user.getFavorites().contains(song));
        Assertions.assertEquals(0, song.getLikes());
    }

    @Test
    void testGetFavoriteSongs()
    {
        user.getFavorites().add(song);
        Set<Song> favorites = favoriteService.getFavoriteSongs(user);
        Assertions.assertTrue(favorites.contains(song));
    }
}
