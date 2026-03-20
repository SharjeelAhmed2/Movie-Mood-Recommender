package com.movie.recommendations.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class MoodEntry {

    @Id
    @GeneratedValue
    private UUID id;

    private OffsetDateTime timestamp = OffsetDateTime.now();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMoodText() {
        return moodText;
    }

    public void setMoodText(String moodText) {
        this.moodText = moodText;
    }

    public String getMoodTag() {
        return moodTag;
    }

    public void setMoodTag(String moodTag) {
        this.moodTag = moodTag;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    @Column(nullable = false)
    private String moodText;

    private String moodTag;       // e.g. "warm", "lonely"

    private String movieId;
    private String movieTitle;

    public String getFilmKey() {
        return filmKey;
    }

    public void setFilmKey(String filmKey) {
        this.filmKey = filmKey;
    }

    private String filmKey;

    // ---- getters & setters (boilerplate) ----
}
