package com.saimone.jdbctemplateapi.actor;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ActorRowMapper implements RowMapper<Actor> {
    @Override
    public Actor mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Actor(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                List.of()
        );
    }
}
