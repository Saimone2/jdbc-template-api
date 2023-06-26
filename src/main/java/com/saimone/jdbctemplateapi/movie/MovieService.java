package com.saimone.jdbctemplateapi.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieDao movieDao;

    public List<Movie> getMovies() {
        return movieDao.selectMovies();
    }
}
