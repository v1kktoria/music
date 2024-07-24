package com.example.music.service;

import com.example.music.dto.Message;
import com.example.music.model.Song;
import com.example.music.model.Comment;
import com.example.music.repository.SongRepository;
import com.example.music.repository.CommentRepository;
import com.example.music.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private SongRepository songRepository;
    @Override
    public List<Comment> findBySongOrderByCreatedAtDesc(Long songId) {
        Song song = songRepository.findById(songId).orElseThrow(
                () -> new RuntimeException("Song not found")
        );
        return commentRepository.findBySongOrderByCreatedAtDesc(song);
    }

    @Override
    public Message addComment(Message message) {
        Comment comment = new Comment();
        Song song = songRepository.findById(message.getSongId()).orElseThrow(
                () -> new RuntimeException("Song not found")
        );
        String username = userRepository.findByEmail(message.getUsername()).getUsername();
        comment.setSong(song);
        comment.setContent(message.getContent());
        comment.setUsername(username);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        comment.setCreatedAt(LocalDateTime.now().format(formatter));

        commentRepository.save(comment);
        message.setCreatedAt(comment.getCreatedAt());
        message.setUsername(username);
        return message;
    }
}
