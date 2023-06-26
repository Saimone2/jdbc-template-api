package com.saimone.jdbctemplateapi.movie;

import java.util.List;

public interface MovieDao {
    List<Movie> selectMovies();
}
