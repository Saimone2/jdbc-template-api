package com.saimone.jdbctemplateapi.movie;

import com.saimone.jdbctemplateapi.exception.NotFoundException;
import com.saimone.jdbctemplateapi.request.MovieRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieDao movieDao;

    public List<Movie> selectAllMovies() {
        return movieDao.selectAllMovies();
    }

    public Movie getMovieById(Integer id) {
        return movieDao.selectMovieById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Movie with id: %s not found", id)));
    }

    public void addNewMovie(MovieRequest request) {
        Movie movie = Movie.builder()
                .name(request.name())
                .releaseDate(request.releaseDate())
                .build();
        movieDao.insertNewMovie(movie);
    }

    public void updateMovie(Integer id, MovieRequest request) {
        Optional<Movie> movies = movieDao.selectMovieById(id);
        movies.ifPresentOrElse(movie -> {
            Movie updatedMovie = Movie.builder()
                    .id(id)
                    .name(request.name())
                    .releaseDate(request.releaseDate())
                    .build();
            movieDao.updateMovie(id, updatedMovie);
        }, () -> {
            throw new NotFoundException(String.format("IN updateMovie - movie with id: %s not found", id));
        });
    }

    public void deleteMovie(Integer id) {
        Optional<Movie> movies = movieDao.selectMovieById(id);
        movies.ifPresentOrElse(movie -> movieDao.deleteMovie(id), () -> {
            throw new NotFoundException(String.format("IN deleteMovie - movie with id: %s not found", id));
        });
    }
}
