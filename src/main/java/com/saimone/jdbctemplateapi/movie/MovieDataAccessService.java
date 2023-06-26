package com.saimone.jdbctemplateapi.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovieDataAccessService implements MovieDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Movie> selectAllMovies() {
        var sql = """
                SELECT id, name, release_date
                FROM movie
                LIMIT 100;
                """;
        return jdbcTemplate.query(sql, new MovieRowMapper());
    }

    @Override
    public Optional<Movie> selectMovieById(Integer id) {
        var sql = """
                SELECT id, name, release_date
                FROM movie
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, new MovieRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertNewMovie(Movie movie) {
        var sql = """
                INSERT INTO movie(name, release_date)
                VALUES (?, ?);
                """;
        jdbcTemplate.update(sql, movie.getName(), movie.getReleaseDate());
    }

    @Override
    public void updateMovie(Integer id, Movie updatedMovie) {
        var sql = """
                UPDATE movie
                SET name = ?, release_date = ?
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, updatedMovie.getName(), updatedMovie.getReleaseDate(), id);
    }

    @Override
    public void deleteMovie(Integer id) {
        var sql = """
                DELETE FROM movie
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Movie> movieListByActorId(Integer id) {
        var sql = """
                SELECT movie.id, movie.name, movie.release_date
                FROM movie
                INNER JOIN actor_movie on movie.id = actor_movie.moveid
                INNER JOIN actor on actor_movie.actorid = actor.id
                WHERE actor.id = ?;
                """;
        return jdbcTemplate.query(sql, new MovieRowMapper(), id);
    }
}
