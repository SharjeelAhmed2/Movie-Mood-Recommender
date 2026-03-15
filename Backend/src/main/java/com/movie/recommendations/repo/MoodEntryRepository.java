package com.movie.recommendations.repo;


import com.movie.recommendations.model.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MoodEntryRepository extends JpaRepository<MoodEntry, UUID> {}
