package com.saimone.jdbctemplateapi.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> allMoviesList() {
        return movieService.getMovies();
    }
}
