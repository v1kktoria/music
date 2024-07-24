package com.example.music.controller;

import com.example.music.model.Song;
import com.example.music.model.User;
import com.example.music.service.SongService;
import com.example.music.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class SongController {
    private SongService songService;
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("songs", songService.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String addSongForm() {
        return "addSong";
    }

    @PostMapping("/addSong")
    public String addSong(@RequestParam String title, @RequestParam String artist, @RequestParam String genre,
                           @RequestParam("audioFile") MultipartFile file, @RequestParam String imageURL, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        User user = userService.findByEmail(userDetails.getUsername());
        songService.saveSong(title, artist, genre, imageURL, file, user);
        return "redirect:/";
    }

    @GetMapping("/mySongs")
    private String mySongs(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("songs", songService.findSongsByUser(user));
        return "mySongs";
    }

    @DeleteMapping("/delete")
    public String deleteSong(@RequestParam Long songId) {
        songService.deleteSong(songId);
        return "redirect:/mySongs";
    }

    @GetMapping("/edit")
    public String editSongForm(@RequestParam Long songId, Model model) {
        Song song = songService.findSongById(songId);
        model.addAttribute("song", song);
        return "editSong";
    }

    @PutMapping("/edit")
    public String updateSong(@RequestParam Long id, @RequestParam String title, @RequestParam String artist,
                              @RequestParam String genre, @RequestParam("audioFile") MultipartFile file) throws IOException {
        songService.updateSong(id, title, artist, genre, file);
        return "redirect:/mySongs";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Song> songs = songService.searchSongs(query);
        model.addAttribute("songs", songs);
        return "index";
    }
}
