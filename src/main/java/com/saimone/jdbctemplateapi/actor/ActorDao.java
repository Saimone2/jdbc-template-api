package com.saimone.jdbctemplateapi.actor;

import java.util.List;
import java.util.Optional;

public interface ActorDao {
    List<Actor> selectAllActors();
    Optional<Actor> selectActorById(Integer id);
    void insertNewActor(Actor actor);
    void updateActor(Integer id, Actor actor);
    void deleteActor(Integer id);
    List<Actor> findSameActors(String name);
    List<Actor> actorListByMovieId(Integer id);
    Optional<Actor> findActorByName(String name);
}
