package com.example.music.controller;

import com.example.music.model.User;
import com.example.music.service.SongService;
import com.example.music.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private UserService userService;
    private SongService songService;

    @GetMapping("/{userId}")
    private String profile(@PathVariable Long userId, Principal principal, Model model) throws Exception {
        User user = userService.findById(userId);
        boolean isOwner = Objects.equals(userId, userService.findByEmail(principal.getName()).getId());
        boolean isFollow = userService.isFollow(userService.findByEmail(principal.getName()).getId(), userId);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("songs", songService.findSongsByUser(user));
        model.addAttribute("user", user);
        model.addAttribute("followers", userService.getFollowers(userId));
        model.addAttribute("following", userService.getFollowing(userId));
        model.addAttribute("isFollow", isFollow);
        return "profile";
    }

    @PostMapping("/update")
    private String update(@RequestParam String avatarUrl, @RequestParam String backgroundUrl, @RequestParam String username, @RequestParam String country, Principal principal) throws Exception {
        Long id = userService.findByEmail(principal.getName()).getId();
        userService.updateUserProfile(id, avatarUrl, backgroundUrl, username, country);
        return "redirect:/profile/" + id;
    }

    @PostMapping("/follow/{targetUserId}")
    public String follow(@PathVariable Long targetUserId, Principal principal) throws Exception {
        Long currentUserId = userService.findByEmail(principal.getName()).getId();
        userService.follow(currentUserId, targetUserId);
        return "redirect:/profile/" + targetUserId;
    }

    @PostMapping("/unfollow/{targetUserId}")
    public String unfollow(@PathVariable Long targetUserId, Principal principal) throws Exception {
        Long currentUserId = userService.findByEmail(principal.getName()).getId();
        userService.removeFollow(currentUserId, targetUserId);
        return "redirect:/profile/" + targetUserId;
    }
}
