package com.movie.recommendations.provider.dto;

import java.util.Map;

public record MovieDto(String id,
                       String title,
                       int year,
                       String posterUrl) {}

