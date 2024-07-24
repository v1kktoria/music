package com.example.music.controller;

import com.example.music.model.Song;
import com.example.music.model.User;
import com.example.music.service.SongService;
import com.example.music.service.FavoriteService;
import com.example.music.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {
    private FavoriteService favoriteService;
    private UserService userService;
    private SongService songService;

    @PostMapping("/add")
    public String addFavorite(@RequestParam Long songId, Principal principal){
        User user = userService.findByEmail(principal.getName());
        Song song = songService.findSongById(songId);
        favoriteService.addFavorite(user, song);
        return "redirect:/";
    }

    @DeleteMapping ("/delete")
    public String removeFavorite(@RequestParam Long songId, Principal principal)
    {
        User user = userService.findByEmail(principal.getName());
        Song song = songService.findSongById(songId);
        favoriteService.removeFavorite(user, song);
        return "redirect:/favorite";
    }

    @GetMapping("")
    public String favorites(Principal principal, Model model)
    {
        User user = userService.findByEmail(principal.getName());
        Set<Song> favorites = favoriteService.getFavoriteSongs(user);
        model.addAttribute("favorites", favorites);
        return "favorites";
    }
}
