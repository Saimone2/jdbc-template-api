package com.saimone.jdbctemplateapi.actor;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActorAccessService implements ActorDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Actor> selectAllActors() {
        var sql = """
                SELECT id, name
                FROM actor
                LIMIT 100
                """;
        return jdbcTemplate.query(sql, new ActorRowMapper());
    }

    @Override
    public Optional<Actor> selectActorById(Integer id) {
        var sql = """
                SELECT id, name
                FROM actor
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, new ActorRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertNewActor(Actor actor) {
        var sql = """
                INSERT INTO actor(name)
                VALUES (?)
                """;
        jdbcTemplate.update(sql, actor.getName());
    }

    @Override
    public void updateActor(Integer id, Actor actor) {
        var sql = """
                UPDATE actor
                SET name = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, actor.getName(), id);
    }

    @Override
    public void deleteActor(Integer id) {
        var sql = """
                DELETE FROM actor
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
    }
}
