package com.example.music.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String content;
    private String username;
    private Long songId;
    private String createdAt;
}
