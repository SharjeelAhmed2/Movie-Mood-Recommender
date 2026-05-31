package com.movie.recommendations.controller;

import com.movie.recommendations.model.WatchedFilm;
import com.movie.recommendations.repo.WatchedFilmRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;      // ✅
import org.springframework.data.domain.PageRequest;   // ✅
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiaryController {

    private final WatchedFilmRepository repo;

    public DiaryController(WatchedFilmRepository r) { this.repo = r; }

    @GetMapping("/diary")
    public List<WatchedFilm> diary(@RequestParam(defaultValue = "30") int limit) {
        return repo
                .findAllByOrderByWatchedAtDesc(PageRequest.of(0, limit))
                .getContent();
    }
}
