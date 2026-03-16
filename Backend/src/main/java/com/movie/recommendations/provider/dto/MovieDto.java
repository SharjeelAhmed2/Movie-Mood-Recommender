package com.movie.recommendations.provider.dto;

public record MovieDto(String id,
                       String title,
                       int year,
                       String posterUrl) {}