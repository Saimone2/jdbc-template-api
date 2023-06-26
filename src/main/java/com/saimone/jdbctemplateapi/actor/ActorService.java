package com.saimone.jdbctemplateapi.actor;

import com.saimone.jdbctemplateapi.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorDao actorDao;

    public List<Actor> selectAllActors() {
        return actorDao.selectAllActors();
    }

    public Actor getActorById(Integer id) {
        return actorDao.selectActorById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Actor with id: %s not found", id)));
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
}
