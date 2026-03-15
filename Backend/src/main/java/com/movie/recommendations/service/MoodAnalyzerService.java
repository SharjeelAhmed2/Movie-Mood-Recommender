package com.movie.recommendations.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MoodAnalyzerService {

    // super-tiny keyword → tag map (expand later)
    private static final Map<String, String> LEXICON = Map.of(
            "lonely", "melancholy",
            "sad",    "melancholy",
            "happy",  "uplifting",
            "nostalgic", "warm",
            "cozy",   "warm",
            "angry",  "intense"
    );

    public String classify(String moodText) {
        return LEXICON.entrySet().stream()
                .filter(e -> moodText.toLowerCase().contains(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("mixed");
    }
}