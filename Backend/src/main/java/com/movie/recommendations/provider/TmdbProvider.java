package com.movie.recommendations.provider;


import com.movie.recommendations.provider.dto.MovieDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Random;

/** Queries TMDB and returns a single random match for a mood-genre tag */
@Component
public class TmdbProvider {

    private final RestTemplate rest = new RestTemplate();
    private final String apiKey;
    private final Random rnd = new Random();

    // mood → TMDB genre-id map
    private static final Map<String, Integer> GENRE = Map.of(
            "melancholy", 18,      // Drama
            "uplifting", 35,       // Comedy
            "warm",       10749,   // Romance
            "intense",    53       // Thriller
    );

    public TmdbProvider(@Value("${tmdb}") String apiKey) {
        this.apiKey = apiKey;
    }

    public MovieDto pickForTag(String tag) {
        int genreId = GENRE.getOrDefault(tag, 18);
        String url = UriComponentsBuilder.fromUriString(
                        "https://api.themoviedb.org/3/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("with_genres", genreId)
                .queryParam("sort_by", "vote_count.desc")  // popular first
                .queryParam("language", "en-US")
                .build().toString();

        var node = rest.getForObject(url, com.fasterxml.jackson.databind.JsonNode.class);
        var results = node.get("results");
        var pick   = results.get(rnd.nextInt(Math.min(20, results.size()))); // first 20 only

        return new MovieDto(
                pick.get("id").asText(),
                pick.get("title").asText(),
                Integer.parseInt(pick.get("release_date").asText().substring(0, 4)),
                "https://image.tmdb.org/t/p/w500" + pick.get("poster_path").asText());
    }
}