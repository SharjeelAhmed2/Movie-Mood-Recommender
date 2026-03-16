package com.movie.recommendations.service;

import com.movie.recommendations.provider.TmdbProvider;
import com.movie.recommendations.provider.dto.MovieDto;
import com.movie.recommendations.repo.WatchedFilmRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MovieProviderService {

    private final TmdbProvider tmdb;
    private final WatchedFilmRepository watchedRepo;     // ➊ add

    public MovieProviderService(TmdbProvider tmdb,
                                WatchedFilmRepository watchedRepo) {  // ➋ add param
        this.tmdb = tmdb;
        this.watchedRepo = watchedRepo;                  // ➌ wire field
    }

    public Map<String,String> pickForTag(String tag) {
        for (int i = 0; i < 5; i++) {              // try up to 5 randoms
            MovieDto m = tmdb.pickForTag(tag);
            if (!watchedRepo.existsByFilmKey(m.id())) {
                return Map.of(
                        "id", m.id(), "title", m.title(),
                        "year", String.valueOf(m.year()),
                        "posterUrl", m.posterUrl());
            }
        }
        // fallback if all random picks were already watched
        return Map.of("id","228","title","Lost in Translation","year","2003");
    }
}