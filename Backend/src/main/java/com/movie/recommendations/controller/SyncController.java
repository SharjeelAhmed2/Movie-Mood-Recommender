package com.movie.recommendations.controller;

import com.movie.recommendations.sync.LetterboxdSyncService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// src/main/java/com/example/moodrecommender/controller/SyncController.java
@RestController
@RequestMapping("/sync")
public class SyncController {

    private final LetterboxdSyncService lbx;
    public SyncController(LetterboxdSyncService lbx) { this.lbx = lbx; }

    @PostMapping("/letterboxd/{user}")
    public String syncDiary(@PathVariable String user) throws Exception {
        int added = lbx.refresh(user);
        return "Imported " + added + " new films from " + user;
    }
}