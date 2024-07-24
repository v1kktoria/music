package com.example.music.repository;

import com.example.music.model.Song;
import com.example.music.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByFavoritesContaining(Song song);
}
