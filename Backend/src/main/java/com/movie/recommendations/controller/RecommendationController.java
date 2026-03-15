package com.movie.recommendations.controller;

import com.movie.recommendations.model.MoodEntry;
import com.movie.recommendations.repo.MoodEntryRepository;
import com.movie.recommendations.service.MoodAnalyzerService;
import com.movie.recommendations.provider.MovieProviderService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RecommendationController {

    private final MoodAnalyzerService moodAnalyzer;
    private final MovieProviderService movieProvider;
    private final MoodEntryRepository repo;

    public RecommendationController(MoodAnalyzerService m, MovieProviderService p, MoodEntryRepository r) {
        this.moodAnalyzer = m;
        this.movieProvider = p;
        this.repo = r;
    }

    @PostMapping("/recommend")
    public Map<String, Object> recommend(@RequestBody Map<String, String> payload) {
        String moodText = payload.getOrDefault("moodText", "");
        String tag = moodAnalyzer.classify(moodText);
        Map<String, String> movie = movieProvider.pickForTag(tag);

        // persist log
        MoodEntry entry = new MoodEntry();
        entry.setMoodText(moodText);
        entry.setMoodTag(tag);
        entry.setMovieId(movie.get("id"));
        entry.setMovieTitle(movie.get("title"));
        repo.save(entry);

        return Map.of(
                "moodTag", tag,
                "movie", movie,
                "quip", "Lila says: cue the popcorn & press play 🍿"
        );
    }
}