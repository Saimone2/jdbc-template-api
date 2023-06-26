package com.saimone.jdbctemplateapi.actor;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/actors")
public class ActorController {
    private final ActorService actorService;

    @GetMapping
    public List<Actor> allActorsList() {
        return actorService.selectAllActors();
    }

    @GetMapping("{id}")
    public Actor getActorById(@PathVariable("id") Integer id) {
        return actorService.getActorById(id);
    }

    @PostMapping
    public void addNewActor(@RequestBody Actor actor) {
        actorService.addNewActor(actor);
    }

    @PostMapping("{id}")
    public void updateActor(@PathVariable("id") Integer id, @RequestBody Actor actor) {
        actorService.updateActor(id, actor);
    }

    @DeleteMapping("{id}")
    public void deleteActor(@PathVariable("id") Integer id) {
        actorService.deleteActor(id);
    }
}
