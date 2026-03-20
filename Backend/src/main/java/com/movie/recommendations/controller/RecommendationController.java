package com.movie.recommendations.controller;

import com.movie.recommendations.model.MoodEntry;
import com.movie.recommendations.repo.MoodEntryRepository;
import com.movie.recommendations.service.MoodAnalyzerService;
import com.movie.recommendations.service.MovieProviderService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
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
    public Map<String, Object> recommend(
            @RequestBody Map<String, String> payload,
            @RequestParam(defaultValue = "1") int count) {   // ➊ add this line

        String moodText = payload.getOrDefault("moodText", "");
        String tag      = moodAnalyzer.classify(moodText);

        //var movies = movieProvider.pickBatch(tag, count);    // ➋ batch call
        var movies = movieProvider.pickBatch(tag, count);
        movies.forEach(m -> {                                // ➌ log each pick
            MoodEntry e = new MoodEntry();
            e.setMoodText(moodText);
            e.setMoodTag(tag);
            e.setFilmKey(m.id());
            e.setMovieTitle(m.title());
            repo.save(e);
        });

        return Map.of(
                "moodTag", tag,
                "movies", movies,
                "quip", "Lila says: binge responsibly 🍿"
        );
    }
}