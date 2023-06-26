package com.saimone.jdbctemplateapi.actor;

import com.saimone.jdbctemplateapi.exception.NotFoundException;
import com.saimone.jdbctemplateapi.movie.Movie;
import com.saimone.jdbctemplateapi.movie.MovieDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorDao actorDao;
    private final MovieDao movieDao;

    public List<Actor> selectAllActors() {
        List<Actor> actors = new ArrayList<>();
        for(Actor actor : actorDao.selectAllActors()) {
            actors.add(addMoviesInfo(actor));
        }
        return actors;
    }

    public Actor getActorById(Integer id) {
        Actor actor = actorDao.selectActorById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Actor with id: %s not found", id)));
        return addMoviesInfo(actor);
    }

    public void addNewActor(Actor actor) {
        actorDao.insertNewActor(actor);
    }

    public void updateActor(Integer id, Actor actor) {
        actorDao.updateActor(id, actor);
    }

    public void deleteActor(Integer id) {
        actorDao.deleteActor(id);
    }

    public Actor addMoviesInfo(Actor actor) {
        List<Movie> movies = movieDao.movieListByActorId(actor.getId());
        List<String> names = new ArrayList<>();
        for(Movie movie : movies) {
            names.add(movie.getName());
        }
        actor.setMovies(names);
        return actor;
    }
}
