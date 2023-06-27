package com.saimone.jdbctemplateapi.movie;

import com.saimone.jdbctemplateapi.actor.Actor;
import com.saimone.jdbctemplateapi.actor.ActorDao;
import com.saimone.jdbctemplateapi.exception.AlreadyExistsException;
import com.saimone.jdbctemplateapi.exception.DuplicateException;
import com.saimone.jdbctemplateapi.exception.NotFoundException;
import com.saimone.jdbctemplateapi.request.MovieRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieDao movieDao;
    private final ActorDao actorDao;

    public List<Movie> selectAllMovies() {
        List<Movie> movies = movieDao.selectAllMovies();
        if(movies == null || movies.isEmpty()) {
            throw new NotFoundException("IN selectAllMovies - no movies found");
        }
        List<Movie> completedMovies = new ArrayList<>();
        for(Movie movie : movies) {
            completedMovies.add(addActorsInfo(movie));
        }
        return completedMovies;
    }

    public Movie getMovieById(Integer id) {
        Movie movie = movieDao.selectMovieById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Movie with id: %s not found", id)));
        return addActorsInfo(movie);
    }

    public void addNewMovie(MovieRequest request) {
        Movie movie = Movie.builder()
                .name(request.name())
                .releaseDate(request.releaseDate())
                .build();
        List<Movie> movies = movieDao.findSameMovies(movie);
        if (movies == null || movies.isEmpty()) {
            List<Actor> actors = extractActorsFromNames(request);
            movieDao.insertNewMovie(movie);
            connectActorToMovie(movie, actors);
        } else {
            throw new AlreadyExistsException(String.format("IN addNewMovie - movie: %s is already in the database", movie.getName()));
        }
    }

    public void updateMovie(Integer id, MovieRequest request) {
        Optional<Movie> movies = movieDao.selectMovieById(id);
        movies.ifPresentOrElse(movie -> {
            Movie updatedMovie = Movie.builder()
                    .name(request.name())
                    .releaseDate(request.releaseDate())
                    .build();
            movieDao.updateMovie(id, updatedMovie);
            movieDao.deleteOldConnections(id);
            List<Actor> actors = extractActorsFromNames(request);
            connectActorToMovie(movie, actors);
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

    private Movie addActorsInfo(Movie movie) {
        List<Actor> actors = actorDao.actorListByMovieId(movie.getId());
        List<String> names = new ArrayList<>();
        for(Actor actor : actors) {
            names.add(actor.getName());
        }
        movie.setActors(names);
        return movie;
    }

    private void connectActorToMovie(Movie movie, List<Actor> actors) {
        List<Movie> movies = movieDao.findSameMovies(movie);
        if(movies.size() == 1) {
            Integer id = movies.get(0).getId();
            for(Actor actor : actors) {
                movieDao.connectActorToMovie(id, actor.getId());
            }
        }
    }

    private List<Actor> extractActorsFromNames(MovieRequest request) {
        Set<String> set = new HashSet<>();
        request.actors().stream().filter(n -> !set.add(n)).forEach(n -> {
            throw new DuplicateException(String.format("IN addNewMovie/updateMovie - cannot be entered twice by the same actor: %s", n));
        });
        List<Actor> actors = new ArrayList<>();
        List<String> names = request.actors();
        for(String name : names) {
            Actor actor = actorDao.findActorByName(name).orElseThrow(() -> new NotFoundException(String.format("IN addNewMovie/updateMovie - actor with name: %s not found", name)));
            actors.add(actor);
        }
        return actors;
    }
}
