package com.movie.recommendations.service;

import com.movie.recommendations.provider.TmdbProvider;
import com.movie.recommendations.provider.dto.MovieDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MovieProviderService {

    private final TmdbProvider tmdb;

    public MovieProviderService(TmdbProvider tmdb) { this.tmdb = tmdb; }

    public Map<String, String> pickForTag(String tag) {
        try {
            MovieDto m = tmdb.pickForTag(tag);
            return Map.of(
                    "id", m.id(),
                    "title", m.title(),
                    "year", String.valueOf(m.year()),
                    "posterUrl", m.posterUrl());
        } catch (Exception e) {
            // fallback: hard-coded stub
            return Map.of("id","228","title","Lost in Translation","year","2003");
        }
    }
}