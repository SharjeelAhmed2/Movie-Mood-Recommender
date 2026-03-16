package com.movie.recommendations.sync;

// src/main/java/com/example/moodrecommender/sync/LetterboxdSyncService.java

import com.movie.recommendations.model.WatchedFilm;
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

    // namespaces we need
    private static final Namespace LB_NS   =
            Namespace.getNamespace("letterboxd", "https://letterboxd.com/");
    private static final Namespace TMDB_NS =
            Namespace.getNamespace("tmdb", "https://themoviedb.org/");

    public LetterboxdSyncService(WatchedFilmRepository repo) {
        this.watchedRepo = repo;
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

            /* ---------- 3️⃣  save if unseen ---------- */
            if (!watchedRepo.existsByFilmKey(filmKey)) {
                WatchedFilm w = new WatchedFilm();
                w.setFilmKey(filmKey);
                w.setWatchedAt(
                        entry.getPublishedDate() != null
                                ? entry.getPublishedDate().toInstant().atOffset(ZoneOffset.UTC)
                                : OffsetDateTime.now());
                watchedRepo.save(w);
                newCount++;
            }
        }
        return newCount;
    }
}