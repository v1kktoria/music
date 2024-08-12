package com.example.music.service;

import com.example.music.model.Song;
import com.example.music.model.User;
import com.example.music.repository.CommentRepository;
import com.example.music.repository.SongRepository;
import com.example.music.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SongServiceImplTest {
    @Mock
    private SongRepository songRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private SongServiceImpl songService;
    private final Path uploadDir = Paths.get("C:\\uploads");
    private User user;
    private Song song;
    @BeforeEach
    void setUp() throws IOException {
         user = new User();
         song = new Song();
        MockitoAnnotations.openMocks(this);
        Files.createDirectories(uploadDir);
    }

    @Test
    void testSaveSong() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getOriginalFilename()).thenReturn("song.mp3");
        songService.saveSong("Title", "Artist", "Genre", "UTL", file, user);
        Mockito.verify(songRepository, Mockito.times(1)).save(Mockito.any(Song.class));
    }

    @Test
    void testUpdateSong() throws IOException {
        song.setAudioPath("song.mp3");
        Mockito.when(songRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(song));
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.isEmpty()).thenReturn(false);
        Mockito.when(file.getOriginalFilename()).thenReturn("newSong.mp3");
        songService.updateSong(1L, "New Title", "New Artist", "New Genre", file);
        Mockito.verify(songRepository, Mockito.times(1)).save(Mockito.any(Song.class));
    }

    @Test
    void testDeleteSong()
    {
        song.setId(1L);
        Mockito.when(songRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(song));
        Mockito.when(userRepository.findAllByFavoritesContaining(Mockito.any(Song.class))).thenReturn(Collections.emptyList());
        songService.deleteSong(1L);
        Mockito.verify(songRepository, Mockito.times(1)).delete(Mockito.any(Song.class));
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(User.class));
    }
    @Test
    void testFindSongById()
    {
        Mockito.when(songRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(song));
        Song result = songService.findSongById(1L);
        Assertions.assertNotNull(result);
        Mockito.verify(songRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testFindALL()
    {
        List<Song> songs = Arrays.asList(new Song(), new Song());
        Mockito.when(songRepository.findAll(Mockito.any(Sort.class))).thenReturn(songs);
        Mockito.when(commentRepository.findBySongOrderByCreatedAtDesc(Mockito.any(Song.class))).thenReturn(Collections.emptyList());
        List<Song> result = songService.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Mockito.verify(songRepository, Mockito.times(1)).findAll(Mockito.any(Sort.class));
    }

    @Test
    void testSearchSongs()
    {
        List<Song> songs = Arrays.asList(new Song(), new Song());
        Mockito.when(songRepository.searchSongs(Mockito.anyString())).thenReturn(songs);
        List<Song> result = songService.searchSongs("searchTerm");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Mockito.verify(songRepository, Mockito.times(1)).searchSongs(Mockito.anyString());
    }

}
