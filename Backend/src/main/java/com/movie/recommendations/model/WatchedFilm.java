package com.movie.recommendations.model;

// src/main/java/com/example/moodrecommender/model/WatchedFilm.java

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class WatchedFilm {

    @Id @GeneratedValue private UUID id;

    // rename from letterboxdId → filmKey
    @Column(unique = true, nullable = false)
    private String filmKey;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilmKey() {
        return filmKey;
    }

    public void setFilmKey(String filmKey) {
        this.filmKey = filmKey;
    }

    public OffsetDateTime getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(OffsetDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }

    private OffsetDateTime watchedAt;

    /* getters + setters */
}
