package com.movie.recommendations.repo;

// src/main/java/com/example/moodrecommender/repo/WatchedFilmRepository.java

import com.movie.recommendations.model.WatchedFilm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WatchedFilmRepository
        extends JpaRepository<WatchedFilm, UUID> {

    boolean existsByFilmKey(String filmKey);
}