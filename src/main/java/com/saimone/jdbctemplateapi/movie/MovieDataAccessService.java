package com.saimone.jdbctemplateapi.movie;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieDataAccessService implements MovieDao {
    private final JdbcTemplate jdbcTemplate;

    public MovieDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Movie> selectMovies() {
        var sql = """
                SELECT id, name, release_date
                FROM movie
                LIMIT 100;
                 """;
        return jdbcTemplate.query(sql, new MovieRowMapper());
    }
}
