package com.saimone.jdbctemplateapi.actor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.saimone.jdbctemplateapi.exception.AlreadyExistsException;
import com.saimone.jdbctemplateapi.exception.NotFoundException;
import com.saimone.jdbctemplateapi.movie.Movie;
import com.saimone.jdbctemplateapi.movie.MovieDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = ActorService.class)
@ExtendWith(SpringExtension.class)
class ActorServiceTest {

    @MockBean
    private ActorDao actorDao;

    @Autowired
    private ActorService actorService;

    @MockBean
    private MovieDao movieDao;

    @Test
    void testSelectAllActors() {
        // when
        when(actorDao.selectAllActors()).thenReturn(new ArrayList<>());

        // then
        assertThrows(NotFoundException.class, () -> actorService.selectAllActors());
        verify(actorDao).selectAllActors();
    }

    @Test
    void testSelectAllActors1() {
        // given
        ArrayList<Actor> actorList = new ArrayList<>();
        ArrayList<Movie> movieList = new ArrayList<>();
        actorList.add(new Actor());

        // when
        when(actorDao.selectAllActors()).thenReturn(actorList);
        when(movieDao.movieListByActorId(Mockito.<Integer>any())).thenReturn(movieList);
        List<Actor> actualSelectAllActorsResult = actorService.selectAllActors();

        // then
        assertEquals(1, actualSelectAllActorsResult.size());
        verify(actorDao).selectAllActors();
        verify(movieDao).movieListByActorId(Mockito.<Integer>any());
    }

    @Test
    void testSelectAllActors2() {
        // given
        ArrayList<Actor> actorList = new ArrayList<>();
        ArrayList<Movie> movieList = new ArrayList<>();
        actorList.add(new Actor());

        // when
        when(actorDao.selectAllActors()).thenReturn(actorList);
        movieList.add(new Movie());
        when(movieDao.movieListByActorId(Mockito.<Integer>any())).thenReturn(movieList);
        List<Actor> actualSelectAllActorsResult = actorService.selectAllActors();
        assertEquals(1, actualSelectAllActorsResult.size());
        List<String> movies = actualSelectAllActorsResult.get(0).getMovies();

        // then
        assertEquals(1, movies.size());
        assertNull(movies.get(0));
        verify(actorDao).selectAllActors();
        verify(movieDao).movieListByActorId(Mockito.<Integer>any());
    }

    @Test
    void testSelectAllActors3() {
        // given
        ArrayList<Actor> actorList = new ArrayList<>();
        actorList.add(new Actor());

        // when
        when(actorDao.selectAllActors()).thenReturn(actorList);
        when(movieDao.movieListByActorId(Mockito.<Integer>any())).thenThrow(new NotFoundException(""));

        // then
        assertThrows(NotFoundException.class, () -> actorService.selectAllActors());
        verify(actorDao).selectAllActors();
        verify(movieDao).movieListByActorId(Mockito.<Integer>any());
    }

    @Test
    void testGetActorById() {
        // given
        Actor actor = new Actor();
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.of(actor));
        ArrayList<Movie> movieList = new ArrayList<>();

        // when
        when(movieDao.movieListByActorId(Mockito.<Integer>any())).thenReturn(movieList);
        Actor actualActorById = actorService.getActorById(1);

        // then
        assertSame(actor, actualActorById);
        verify(actorDao).selectActorById(Mockito.<Integer>any());
        verify(movieDao).movieListByActorId(Mockito.<Integer>any());
    }

    @Test
    void testGetActorById1() {
        // when
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.of(new Actor()));
        when(movieDao.movieListByActorId(Mockito.<Integer>any())).thenThrow(new NotFoundException(""));

        // then
        assertThrows(NotFoundException.class, () -> actorService.getActorById(1));
        verify(actorDao).selectActorById(Mockito.<Integer>any());
        verify(movieDao).movieListByActorId(Mockito.<Integer>any());
    }

    @Test
    void testGetActorById2() {
        // given
        Actor actor = mock(Actor.class);

        // when
        when(actor.getId()).thenThrow(new AlreadyExistsException("An error occurred"));
        Optional<Actor> ofResult = Optional.of(actor);
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(ofResult);

        // then
        assertThrows(AlreadyExistsException.class, () -> actorService.getActorById(1));
        verify(actorDao).selectActorById(Mockito.<Integer>any());
        verify(actor).getId();
    }

    @Test
    void testAddNewActor() {
        // when
        when(actorDao.findSameActors(Mockito.any())).thenReturn(new ArrayList<>());
        doNothing().when(actorDao).insertNewActor(Mockito.any());
        actorService.addNewActor(new Actor());

        // then
        verify(actorDao).findSameActors(Mockito.any());
        verify(actorDao).insertNewActor(Mockito.any());
    }

    @Test
    void testAddNewActor1() {
        // given
        ArrayList<Actor> actorList = new ArrayList<>();
        actorList.add(new Actor());

        // when
        when(actorDao.findSameActors(Mockito.any())).thenReturn(actorList);

        // then
        assertThrows(AlreadyExistsException.class, () -> actorService.addNewActor(new Actor()));
        verify(actorDao).findSameActors(Mockito.any());
    }

    @Test
    void testAddNewActor2() {
        // when
        when(actorDao.findSameActors(Mockito.any())).thenThrow(new AlreadyExistsException("An error occurred"));

        // then
        assertThrows(AlreadyExistsException.class, () -> actorService.addNewActor(new Actor()));
        verify(actorDao).findSameActors(Mockito.any());
    }

    @Test
    void testUpdateActor() {
        // when
        when(actorDao.findSameActors(Mockito.any())).thenReturn(new ArrayList<>());
        doNothing().when(actorDao).updateActor(Mockito.<Integer>any(), Mockito.any());
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.of(new Actor()));
        actorService.updateActor(1, new Actor());

        // then
        verify(actorDao).findSameActors(Mockito.any());
        verify(actorDao).selectActorById(Mockito.<Integer>any());
        verify(actorDao).updateActor(Mockito.<Integer>any(), Mockito.any());
    }

    @Test
    void testUpdateActor1() {
        // when
        when(actorDao.findSameActors(Mockito.any())).thenThrow(new AlreadyExistsException("An error occurred"));
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.of(new Actor()));

        // then
        assertThrows(AlreadyExistsException.class, () -> actorService.updateActor(1, new Actor()));
        verify(actorDao).findSameActors(Mockito.any());
        verify(actorDao).selectActorById(Mockito.<Integer>any());
    }

    @Test
    void testUpdateActor2() {
        // given
        ArrayList<Actor> actorList = new ArrayList<>();
        actorList.add(new Actor());

        // when
        when(actorDao.findSameActors(Mockito.any())).thenReturn(actorList);
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.of(new Actor()));

        // then
        assertThrows(AlreadyExistsException.class, () -> actorService.updateActor(1, new Actor()));
        verify(actorDao).findSameActors(Mockito.any());
        verify(actorDao).selectActorById(Mockito.<Integer>any());
    }

    @Test
    void testUpdateActor3() {
        // when
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> actorService.updateActor(1, new Actor()));
        verify(actorDao).selectActorById(Mockito.<Integer>any());
    }

    @Test
    void testDeleteActor() {
        // when
        doNothing().when(actorDao).deleteActor(Mockito.<Integer>any());
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.of(new Actor()));
        actorService.deleteActor(1);

        // then
        verify(actorDao).selectActorById(Mockito.<Integer>any());
        verify(actorDao).deleteActor(Mockito.<Integer>any());
    }

    @Test
    void testDeleteActor1() {
        // when
        doThrow(new NotFoundException("")).when(actorDao).deleteActor(Mockito.<Integer>any());
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.of(new Actor()));

        // then
        assertThrows(NotFoundException.class, () -> actorService.deleteActor(1));
        verify(actorDao).selectActorById(Mockito.<Integer>any());
        verify(actorDao).deleteActor(Mockito.<Integer>any());
    }

    @Test
    void testDeleteActor2() {
        // when
        when(actorDao.selectActorById(Mockito.<Integer>any())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> actorService.deleteActor(1));
        verify(actorDao).selectActorById(Mockito.<Integer>any());
    }

    @Test
    void testAddMoviesInfo() {
        // given
        ArrayList<Movie> movieList = new ArrayList<>();
        Actor actor = new Actor();

        // when
        when(movieDao.movieListByActorId(Mockito.<Integer>any())).thenReturn(movieList);
        Actor actualAddMoviesInfoResult = actorService.addMoviesInfo(actor);

        // then
        assertSame(actor, actualAddMoviesInfoResult);
        verify(movieDao).movieListByActorId(Mockito.<Integer>any());
    }

    @Test
    void testAddMoviesInfo1() {
        // given
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie());
        Actor actor = new Actor();

        // when
        when(movieDao.movieListByActorId(Mockito.<Integer>any())).thenReturn(movieList);
        Actor actualAddMoviesInfoResult = actorService.addMoviesInfo(actor);

        // then
        assertSame(actor, actualAddMoviesInfoResult);
        List<String> movies = actualAddMoviesInfoResult.getMovies();
        assertEquals(1, movies.size());
        assertNull(movies.get(0));
        verify(movieDao).movieListByActorId(Mockito.<Integer>any());
    }

    @Test
    void testAddMoviesInfo2() {
        // when
        when(movieDao.movieListByActorId(Mockito.<Integer>any())).thenThrow(new NotFoundException(""));

        // then
        assertThrows(NotFoundException.class, () -> actorService.addMoviesInfo(new Actor()));
        verify(movieDao).movieListByActorId(Mockito.<Integer>any());
    }
}

