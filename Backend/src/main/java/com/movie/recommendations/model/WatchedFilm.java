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

    // NEW fields we hydrate once at sync-time
    private String title;

    private Integer  year;
    private String posterUrl;

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

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
