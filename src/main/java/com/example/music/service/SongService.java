package com.example.music.service;

import com.example.music.model.Song;
import com.example.music.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SongService {
    void saveSong(String title, String artist, String genre, String imageURL, MultipartFile file, User user) throws IOException;
    Set<Song> findSongsByUser(User user);
    void updateSong(Long id, String title, String artist, String genre, MultipartFile file) throws IOException;
    void deleteSong(Long id);
    Song findSongById(Long id);
    List<Song> findAll();
    List<Song> searchSongs(String searchTerm);
}
