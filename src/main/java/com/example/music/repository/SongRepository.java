package com.example.music.repository;

import com.example.music.model.Song;
import com.example.music.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Set<Song> findSongsByUser(User user);
    List<Song> findAll(Sort sort);
    @Query("SELECT a FROM Song a WHERE " +
            "a.artist LIKE %:searchTerm% OR " +
            "a.genre LIKE %:searchTerm% OR " +
            "a.title LIKE %:searchTerm%")
    List<Song> searchSongs(@Param("searchTerm") String searchTerm);
}
