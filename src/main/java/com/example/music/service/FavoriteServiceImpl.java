package com.example.music.service;

import com.example.music.model.Song;
import com.example.music.model.User;
import com.example.music.repository.SongRepository;
import com.example.music.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{
    private UserRepository userRepository;
    private SongRepository songRepository;
    @Override
    public void addFavorite(User user, Song song){
        if (!user.getFavorites().contains(song)){
            user.getFavorites().add(song);
            userRepository.save(user);
            song.setLikes(song.getLikes() + 1);
            songRepository.save(song);
        }
    }

    @Override
    public void removeFavorite(User user, Song song) {
        if (user.getFavorites().contains(song)){
            user.getFavorites().remove(song);
            userRepository.save(user);
            song.setLikes(Math.max(song.getLikes() - 1, 0));
            songRepository.save(song);
        }
    }

    @Override
    public Set<Song> getFavoriteSongs(User user) {
        return user.getFavorites();
    }
}
