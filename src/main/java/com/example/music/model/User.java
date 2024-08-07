package com.example.music.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Song> songs = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<Song> favorites = new HashSet<>();
    private String avatarURL = "https://media.istockphoto.com/id/1495088043/nl/vector/user-profile-icon-avatar-or-person-icon-profile-picture-portrait-symbol-default-portrait.jpg?s=612x612&w=0&k=20&c=XFTwykfMC-DCqALkaQi7pkQrlRvvrNdlDP8ramfdMnU=";
    private String backgroundUrl = "https://wallpapercave.com/wp/m93D0ZU.jpg";
    private String country;
    @ManyToMany
    private Set<User> followers = new HashSet<>();
    @ManyToMany
    private Set<User> following = new HashSet<>();
}
