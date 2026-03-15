package com.movie.recommendations.provider;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MovieProviderService {

    // hard-coded picks by tag for MVP
    private static final Map<String, Map<String, String>> PICKS = Map.of(
            "melancholy", Map.of(
                    "id", "228", "title", "Lost in Translation", "year", "2003"),
            "uplifting", Map.of(
                    "id", "194", "title", "Amélie", "year", "2001"),
            "warm", Map.of(
                    "id", "17647", "title", "The Lunchbox", "year", "2013"),
            "intense", Map.of(
                    "id", "807", "title", "Se7en", "year", "1995"),
            "mixed", Map.of(
                    "id", "11216", "title", "Eternal Sunshine of the Spotless Mind", "year", "2004")
    );

    public Map<String, String> pickForTag(String tag) {
        return PICKS.getOrDefault(tag, PICKS.get("mixed"));
    }
}