package com.example.music.controller;

import com.example.music.dto.Message;
import com.example.music.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class CommentWebSocketController {
    private SimpMessagingTemplate simpMessagingTemplate;
    private CommentService commentService;

    @MessageMapping("/comment")
    public void sendComment(Message message, Principal principal) {
        message.setUsername(principal.getName());
        simpMessagingTemplate.convertAndSend("/topic/comments", commentService.addComment(message));
    }
}
