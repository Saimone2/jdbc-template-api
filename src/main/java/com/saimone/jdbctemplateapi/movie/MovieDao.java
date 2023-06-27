package com.saimone.jdbctemplateapi.movie;

import java.util.List;
import java.util.Optional;

public interface MovieDao {
    List<Movie> selectAllMovies();
    Optional<Movie> selectMovieById(Integer id);
    void insertNewMovie(Movie movie);
    void updateMovie(Integer id, Movie updatedMovie);
    void deleteMovie(Integer id);
    List<Movie> movieListByActorId(Integer id);
    List<Movie> findSameMovies(Movie movie);
    void connectActorToMovie(Integer moveId, Integer actorId);
    void deleteOldConnections(Integer id);
}
