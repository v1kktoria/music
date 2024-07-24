package com.example.music.service;

import com.example.music.model.Song;
import com.example.music.model.User;

import java.util.Set;

public interface FavoriteService {
    void addFavorite(User user, Song song);
    void removeFavorite(User user, Song song);
    Set<Song> getFavoriteSongs(User user);
}
