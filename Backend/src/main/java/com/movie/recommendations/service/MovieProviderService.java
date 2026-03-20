package com.movie.recommendations.service;

import com.movie.recommendations.provider.TmdbProvider;
import com.movie.recommendations.provider.dto.MovieDto;
import com.movie.recommendations.repo.WatchedFilmRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MovieProviderService {

    private final TmdbProvider tmdb;
    private final WatchedFilmRepository watchedRepo;     // ➊ add
    private MovieDto mapToDto(Map<String,String> m) {
        return new MovieDto(
                m.get("id"),
                m.get("title"),
                Integer.parseInt(m.get("year")),
                m.get("posterUrl"));
    }
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
    public List<MovieDto> pickBatch(String tag, int size) {
        List<MovieDto> out = new ArrayList<>();
        int tries = 0;
        while (out.size() < size && tries < 20) {
            MovieDto m = mapToDto(pickForTag(tag));      // 🔄 convert
            if (!watchedRepo.existsByFilmKey(m.id())
                    && out.stream().noneMatch(x -> x.id().equals(m.id())))
                out.add(m);
            tries++;
        }
        return out;
    }
}