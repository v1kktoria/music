package com.example.music.exception;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(String message)
    {
        super(message);
    }
}
