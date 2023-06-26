package com.saimone.jdbctemplateapi.movie;

import com.saimone.jdbctemplateapi.request.MovieRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> allMoviesList() {
        return movieService.selectAllMovies();
    }

    @GetMapping("{id}")
    public Movie getMovieById(@PathVariable("id") Integer id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public void addNewMovie(@RequestBody MovieRequest request) {
        movieService.addNewMovie(request);
    }

    @PostMapping("{id}")
    public void updateMovie(@PathVariable("id") Integer id, @RequestBody MovieRequest request) {
        movieService.updateMovie(id, request);
    }

    @DeleteMapping("{id}")
    public void deleteMovie(@PathVariable("id") Integer id) {
        movieService.deleteMovie(id);
    }
}
