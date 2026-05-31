package com.movie.recommendations.sync;

// src/main/java/com/example/moodrecommender/sync/LetterboxdSyncService.java

import com.movie.recommendations.model.WatchedFilm;
import com.movie.recommendations.provider.TmdbProvider;
import com.movie.recommendations.provider.dto.MovieDto;
import com.movie.recommendations.repo.WatchedFilmRepository;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class LetterboxdSyncService {

    private final WatchedFilmRepository watchedRepo;

    private final TmdbProvider tmdbProvider;
    // namespaces we need
    private static final Namespace LB_NS   =
            Namespace.getNamespace("letterboxd", "https://letterboxd.com/");
    private static final Namespace TMDB_NS =
            Namespace.getNamespace("tmdb", "https://themoviedb.org/");

    public LetterboxdSyncService(WatchedFilmRepository repo, TmdbProvider tmdbProvider) {
        this.watchedRepo = repo;
        this.tmdbProvider = tmdbProvider;
    }

    public int refresh(String username) throws Exception {
        var feed = new SyndFeedInput().build(
                new XmlReader(new URL("https://letterboxd.com/" + username + "/rss/")));

        int newCount = 0;

        for (var entry : feed.getEntries()) {

            /* ---------- 1️⃣  try tmdb:movieId first ---------- */
            Element idEl = entry.getForeignMarkup().stream()
                    .filter(e -> e.getName().equals("movieId")
                            && e.getNamespace().equals(TMDB_NS))
                    .findFirst()
                    .orElse(null);

            /* ---------- 2️⃣  fall back to slug in <link> ---------- */
            String filmKey;
            if (idEl != null) {
                filmKey = idEl.getValue();                       // numeric TMDB id
            } else {
                String link = entry.getLink();                   // …/film/lost-in-translation/
                filmKey = link.replaceAll(".*/film/(.*?)/?$", "$1"); // “lost-in-translation”
            }

            /* ---------- 3️⃣  save if unseen (now HYDRATED) ---------- */
            if (!watchedRepo.existsByFilmKey(filmKey)) {

                // ➊  try to fetch full details from TMDB by ID
                MovieDto dto = null;
                if (filmKey.matches("\\d+")) {                   // only if it’s numeric
                    dto = tmdbProvider.pickById(filmKey);        // add this helper in TmdbProvider
                }

                // ➋  build entity
                WatchedFilm w = new WatchedFilm();
                w.setFilmKey(filmKey);
                w.setWatchedAt(
                        entry.getPublishedDate() != null
                                ? entry.getPublishedDate().toInstant().atOffset(ZoneOffset.UTC)
                                : OffsetDateTime.now());

                if (dto != null) {                              // hydrate when lookup succeeded
                    w.setTitle(dto.title());
                    w.setYear(dto.year());
                    w.setPosterUrl(dto.posterUrl());
                }

                watchedRepo.save(w);
                newCount++;
            }
        }
        return newCount;
    }
}