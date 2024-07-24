package com.example.music.controller;

import com.example.music.model.User;
import com.example.music.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;

    @GetMapping("/signin")
    public String signinForm(Model model) {
        model.addAttribute("user", new User());
        return "signin";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping("/signout")
    public String signout() {
        return "redirect:/auth/signin";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, Model model) throws Exception {
        if (userService.userExists(user.getEmail())) {
            model.addAttribute("error", "User already exists with email " + user.getEmail());
            return "signup";
        }
        userService.saveUser(user);
        return "redirect:/auth/signin";
    }
}
