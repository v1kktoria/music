package com.example.music.repository;

import com.example.music.model.Song;
import com.example.music.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySongOrderByCreatedAtDesc(Song song);
}
