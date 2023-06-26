package com.saimone.jdbctemplateapi.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Movie {
    private Integer id;
    private String name;
    private List<String> actors;
    private LocalDate releaseDate;
}
