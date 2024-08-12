package com.example.music.service;

import com.example.music.exception.SongNotFoundException;
import com.example.music.model.Song;
import com.example.music.model.Comment;
import com.example.music.model.User;
import com.example.music.repository.SongRepository;
import com.example.music.repository.CommentRepository;
import com.example.music.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SongServiceImpl implements SongService {
    private SongRepository songRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private final Path uploadDir = Paths.get("/app/uploads");

    @Override
    public void saveSong(String title, String artist, String genre, String imageURL, MultipartFile file, User user) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "default_file";
        }
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = uploadDir.resolve(uniqueFilename);
        Files.createDirectories(uploadDir);
        file.transferTo(filePath);

        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setGenre(genre);
        song.setAudioPath(uniqueFilename);
        song.setLikes(0);
        song.setUser(user);
        song.setImageURL(imageURL);
        songRepository.save(song);
    }

    @Override
    public Set<Song> findSongsByUser(User user) {
        return songRepository.findSongsByUser(user);
    }

    @Override
    public void updateSong(Long id, String title, String artist, String genre, MultipartFile file) throws IOException {
        Song song = songRepository.findById(id).orElseThrow(
                () -> new SongNotFoundException("Song not found with id: " + id)
        );
        song.setTitle(title);
        song.setArtist(artist);
        song.setGenre(genre);
        if (!file.isEmpty()) {
            Path oldFilePath = uploadDir.resolve(song.getAudioPath());
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "default_file";
            }
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path newFilePath = uploadDir.resolve(uniqueFilename);
            Files.createDirectories(newFilePath.getParent());
            file.transferTo(newFilePath.toFile());
            song.setAudioPath(uniqueFilename);
        }
        songRepository.save(song);
    }

    @Override
    public void deleteSong(Long id) {
        Song song = songRepository.findById(id).orElseThrow(
                () -> new SongNotFoundException("Song not found with id: " + id)
        );
        List<User> usersWithFavorite = userRepository.findAllByFavoritesContaining(song);
        for (User user : usersWithFavorite) {
            user.getFavorites().remove(song);
            userRepository.save(user);
        }
        songRepository.delete(song);
    }

    @Override
    public Song findSongById(Long id) {
        return songRepository.findById(id).orElseThrow(
                () -> new SongNotFoundException("Song not found with id: " + id)
        );
    }

    @Override
    public List<Song> findAll() {
        Sort sort = Sort.by(Sort.Order.asc("id"));
        List<Song> songs = songRepository.findAll(sort);
        songs.forEach(album -> {
            List<Comment> sortedComments = commentRepository.findBySongOrderByCreatedAtDesc(album);
            album.setComments(sortedComments);
        });
        return songs;
    }

    @Override
    public List<Song> searchSongs(String searchTerm) {
        return songRepository.searchSongs(searchTerm);
    }
}
