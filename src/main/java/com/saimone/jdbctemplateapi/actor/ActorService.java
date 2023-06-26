package com.saimone.jdbctemplateapi.actor;

import com.saimone.jdbctemplateapi.exception.AlreadyExistsException;
import com.saimone.jdbctemplateapi.exception.NotFoundException;
import com.saimone.jdbctemplateapi.movie.Movie;
import com.saimone.jdbctemplateapi.movie.MovieDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorDao actorDao;
    private final MovieDao movieDao;

    public List<Actor> selectAllActors() {
        List<Actor> actors = actorDao.selectAllActors();
        if(actors == null || actors.isEmpty()) {
            throw new NotFoundException("IN selectAllActors - no actors found");
        }
        List<Actor> completedActors = new ArrayList<>();
        for(Actor actor : actorDao.selectAllActors()) {
            completedActors.add(addMoviesInfo(actor));
        }
        return completedActors;
    }

    public Actor getActorById(Integer id) {
        Actor actor = actorDao.selectActorById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Actor with id: %s not found", id)));
        return addMoviesInfo(actor);
    }

    public void addNewActor(Actor actor) {
        List<Actor> actors = actorDao.findActorsWithName(actor.getName());
        if (actors == null || actors.isEmpty()) {
            actorDao.insertNewActor(actor);
        } else {
            throw new AlreadyExistsException(String.format("IN addNewActor - such an actor: %s is already in the database", actor.getName()));
        }
    }

    public void updateActor(Integer id, Actor updatedActor) {
        Optional<Actor> actor = actorDao.selectActorById(id);
        actor.ifPresentOrElse((e) -> {
            List<Actor> duplicateActors = actorDao.findActorsWithName(updatedActor.getName());
            if(duplicateActors == null || duplicateActors.isEmpty()) {
                actorDao.updateActor(id, updatedActor);
            } else {
                throw new AlreadyExistsException(String.format("IN updateActor - such an actor: %s is already in the database", updatedActor.getName()));
            }}, () -> {
            throw new NotFoundException(String.format("IN deleteActor - actor with id: %s not found", id));
        });
    }

    public void deleteActor(Integer id) {
        Optional<Actor> actor = actorDao.selectActorById(id);
        actor.ifPresentOrElse((e) -> actorDao.deleteActor(id), () -> {
            throw new NotFoundException(String.format("IN deleteActor - actor with id: %s not found", id));
        });
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
