package com.example.music.service;

import com.example.music.dto.Message;
import com.example.music.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findBySongOrderByCreatedAtDesc(Long albumId);
    Message addComment(Message message);
}
