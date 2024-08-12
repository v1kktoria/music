package com.example.music.service;

import com.example.music.dto.Message;
import com.example.music.exception.SongNotFoundException;
import com.example.music.model.Comment;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SongRepository songRepository;
    private Song song;
    private User user;
    private Comment comment;
    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        song = new Song();
        song.setId(1L);
        user = new User();
        user.setEmail("user@gmail.com");
        user.setUsername("username");
        user.setAvatarURL("avatarUrl");
        comment = new Comment();
        comment.setSong(song);
        comment.setUser(user);
    }

    @Test
    void testFindBySongOrderByCreatedAtDesc()
    {
        Mockito.when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        Mockito.when(commentRepository.findBySongOrderByCreatedAtDesc(song)).thenReturn(Collections.singletonList(comment));
        List<Comment> comments = commentService.findBySongOrderByCreatedAtDesc(1L);
        Assertions.assertNotNull(comments);
        Assertions.assertFalse(comments.isEmpty());
        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(comment, comments.get(0));
        Mockito.verify(songRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).findBySongOrderByCreatedAtDesc(song);
    }

    @Test
    void testFindBySongOrderByCreatedAtDesc_SongNotFound()
    {
        Mockito.when(songRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(SongNotFoundException.class, () -> commentService.findBySongOrderByCreatedAtDesc(1L));
        Mockito.verify(songRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(commentRepository, Mockito.times(0)).findBySongOrderByCreatedAtDesc(Mockito.any(Song.class));
    }

    @Test
    void testAddComment()
    {
        Message message = new Message();
        message.setSongId(1L);
        message.setContent("Content");
        message.setUsername("user@gmail.com");
        Mockito.when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        Mockito.when(userRepository.findByEmail("user@gmail.com")).thenReturn(user);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")));
            return savedComment;
        });
        Message resultMessage = commentService.addComment(message);

        Assertions.assertNotNull(resultMessage);
        Assertions.assertEquals("Content", resultMessage.getContent());
        Assertions.assertEquals("username", resultMessage.getUsername());
        Assertions.assertEquals("avatarUrl", resultMessage.getAvatarUrl());
        Assertions.assertNotNull(resultMessage.getCreatedAt());
        Mockito.verify(songRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("user@gmail.com");
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    void testAddComment_SongNotFound()
    {
        Message message = new Message();
        message.setSongId(1L);
        message.setContent("Content");
        message.setUsername("user@gmail.com");
        Mockito.when(songRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(SongNotFoundException.class, () -> commentService.addComment(message));
        Mockito.verify(songRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(0)).findByEmail(Mockito.anyString());
        Mockito.verify(commentRepository, Mockito.times(0)).save(Mockito.any(Comment.class));
    }


}
