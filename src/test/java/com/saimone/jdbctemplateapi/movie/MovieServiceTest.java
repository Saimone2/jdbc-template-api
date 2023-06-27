package com.saimone.jdbctemplateapi.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.saimone.jdbctemplateapi.actor.Actor;
import com.saimone.jdbctemplateapi.actor.ActorDao;
import com.saimone.jdbctemplateapi.exception.AlreadyExistsException;
import com.saimone.jdbctemplateapi.exception.DuplicateException;
import com.saimone.jdbctemplateapi.exception.NotFoundException;
import com.saimone.jdbctemplateapi.request.MovieRequest;

import java.time.LocalDate;
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

@ContextConfiguration(classes = {MovieService.class})
@ExtendWith(SpringExtension.class)
class MovieServiceTest {
    @MockBean
    private ActorDao actorDao;

    @MockBean
    private MovieDao movieDao;

    @Autowired
    private MovieService movieService;

    @Test
    void testSelectAllMovies() {
        // when
        when(movieDao.selectAllMovies()).thenReturn(new ArrayList<>());

        // then
        assertThrows(NotFoundException.class, () -> movieService.selectAllMovies());
        verify(movieDao).selectAllMovies();
    }

    @Test
    void testSelectAllMovies1() {
        // given
        ArrayList<Movie> movieList = new ArrayList<>();
        ArrayList<Actor> actorList = new ArrayList<>();
        movieList.add(new Movie());

        //when
        when(movieDao.selectAllMovies()).thenReturn(movieList);
        when(actorDao.actorListByMovieId(Mockito.<Integer>any())).thenReturn(actorList);
        List<Movie> actualSelectAllMoviesResult = movieService.selectAllMovies();

        // then
        assertEquals(1, actualSelectAllMoviesResult.size());
        verify(movieDao).selectAllMovies();
        verify(actorDao).actorListByMovieId(Mockito.<Integer>any());
    }

    @Test
    void testSelectAllMovies2() {
        // given
        Movie movie = mock(Movie.class);
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(movie);

        // when
        when(movie.getId()).thenReturn(1);
        doNothing().when(movie).setActors(Mockito.any());
        when(movieDao.selectAllMovies()).thenReturn(movieList);
        when(actorDao.actorListByMovieId(Mockito.<Integer>any())).thenReturn(new ArrayList<>());

        // then
        assertEquals(1, movieService.selectAllMovies().size());
        verify(movieDao).selectAllMovies();
        verify(movie).getId();
        verify(movie).setActors(Mockito.any());
        verify(actorDao).actorListByMovieId(Mockito.<Integer>any());
    }

    @Test
    void testSelectAllMovies3() {
        // given
        Movie movie = mock(Movie.class);
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        ArrayList<Actor> actorList = new ArrayList<>();
        actorList.add(new Actor());

        // when
        when(movie.getId()).thenReturn(1);
        doNothing().when(movie).setActors(Mockito.any());
        when(movieDao.selectAllMovies()).thenReturn(movieList);
        when(actorDao.actorListByMovieId(Mockito.<Integer>any())).thenReturn(actorList);

        // then
        assertEquals(1, movieService.selectAllMovies().size());
        verify(movieDao).selectAllMovies();
        verify(movie).getId();
        verify(movie).setActors(Mockito.any());
        verify(actorDao).actorListByMovieId(Mockito.<Integer>any());
    }

    @Test
    void testGetMovieById() {
        // given
        Movie movie = new Movie();

        // when
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.of(movie));
        ArrayList<Actor> actorList = new ArrayList<>();
        when(actorDao.actorListByMovieId(Mockito.<Integer>any())).thenReturn(actorList);
        Movie actualMovieById = movieService.getMovieById(1);

        // then
        assertSame(movie, actualMovieById);
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(actorDao).actorListByMovieId(Mockito.<Integer>any());
    }

    @Test
    void testGetMovieById1() {
        // when
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.of(new Movie()));
        when(actorDao.actorListByMovieId(Mockito.<Integer>any())).thenThrow(new NotFoundException("An error occurred"));

        // then
        assertThrows(NotFoundException.class, () -> movieService.getMovieById(1));
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(actorDao).actorListByMovieId(Mockito.<Integer>any());
    }

    @Test
    void testGetMovieById2() {
        // given
        Movie movie = mock(Movie.class);

        // when
        when(movie.getId()).thenReturn(1);
        doNothing().when(movie).setActors(Mockito.any());
        Optional<Movie> ofResult = Optional.of(movie);
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(ofResult);
        when(actorDao.actorListByMovieId(Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        movieService.getMovieById(1);

        // then
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(movie).getId();
        verify(movie).setActors(Mockito.any());
        verify(actorDao).actorListByMovieId(Mockito.<Integer>any());
    }

    @Test
    void testAddNewMovie() {
        // given
        ArrayList<String> actors = new ArrayList<>();

        // when
        when(movieDao.findSameMovies(Mockito.any())).thenReturn(new ArrayList<>());
        doNothing().when(movieDao).insertNewMovie(Mockito.any());
        movieService.addNewMovie(new MovieRequest("Name", actors, LocalDate.of(1970, 1, 1)));

        // then
        verify(movieDao, atLeast(1)).findSameMovies(Mockito.any());
        verify(movieDao).insertNewMovie(Mockito.any());
    }

    @Test
    void testAddNewMovie1() {
        // given
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie());
        ArrayList<String> actors = new ArrayList<>();

        // when
        when(movieDao.findSameMovies(Mockito.any())).thenReturn(movieList);

        // then
        assertThrows(AlreadyExistsException.class,
                () -> movieService.addNewMovie(new MovieRequest("Name", actors, LocalDate.of(1970, 1, 1))));
        verify(movieDao).findSameMovies(Mockito.any());
    }

    @Test
    void testAddNewMovie2() {
        // given
        ArrayList<String> actors = new ArrayList<>();
        actors.add("foo");

        // when
        when(movieDao.findSameMovies(Mockito.any())).thenReturn(new ArrayList<>());
        doNothing().when(movieDao).insertNewMovie(Mockito.any());
        when(actorDao.findActorByName(Mockito.any())).thenReturn(Optional.of(new Actor()));
        movieService.addNewMovie(new MovieRequest("Name", actors, LocalDate.of(1970, 1, 1)));

        // then
        verify(movieDao, atLeast(1)).findSameMovies(Mockito.any());
        verify(movieDao).insertNewMovie(Mockito.any());
        verify(actorDao).findActorByName(Mockito.any());
    }

    @Test
    void testUpdateMovie() {
        // given
        ArrayList<String> actors = new ArrayList<>();

        // when
        when(movieDao.findSameMovies(Mockito.any())).thenReturn(new ArrayList<>());
        doNothing().when(movieDao).deleteOldConnections(Mockito.<Integer>any());
        doNothing().when(movieDao).updateMovie(Mockito.<Integer>any(), Mockito.any());
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.of(new Movie()));
        movieService.updateMovie(1, new MovieRequest("Name", actors, LocalDate.of(1970, 1, 1)));

        // then
        verify(movieDao).findSameMovies(Mockito.any());
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(movieDao).deleteOldConnections(Mockito.<Integer>any());
        verify(movieDao).updateMovie(Mockito.<Integer>any(), Mockito.any());
    }

    @Test
    void testUpdateMovie1() {
        // given
        ArrayList<String> actors = new ArrayList<>();

        // when
        doThrow(new DuplicateException("")).when(movieDao)
                .updateMovie(Mockito.<Integer>any(), Mockito.any());
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.of(new Movie()));

        // then
        assertThrows(DuplicateException.class,
                () -> movieService.updateMovie(1, new MovieRequest("Name", actors, LocalDate.of(1970, 1, 1))));
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(movieDao).updateMovie(Mockito.<Integer>any(), Mockito.any());
    }

    @Test
    void testUpdateMovie2() {
        // given
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie());
        ArrayList<String> actors = new ArrayList<>();

        // when
        when(movieDao.findSameMovies(Mockito.any())).thenReturn(movieList);
        doNothing().when(movieDao).deleteOldConnections(Mockito.<Integer>any());
        doNothing().when(movieDao).updateMovie(Mockito.<Integer>any(), Mockito.any());
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.of(new Movie()));
        movieService.updateMovie(1, new MovieRequest("Name", actors, LocalDate.of(1970, 1, 1)));

        // then
        verify(movieDao).findSameMovies(Mockito.any());
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(movieDao).deleteOldConnections(Mockito.<Integer>any());
        verify(movieDao).updateMovie(Mockito.<Integer>any(), Mockito.any());
    }

    @Test
    void testUpdateMovie3() {
        // given
        ArrayList<String> actors = new ArrayList<>();

        // when
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class,
                () -> movieService.updateMovie(1, new MovieRequest("Name", actors, LocalDate.of(1970, 1, 1))));
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
    }

    @Test
    void testUpdateMovie11() {
        // given
        Movie movie = mock(Movie.class);
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        Actor actor = mock(Actor.class);
        ArrayList<String> actors = new ArrayList<>();
        actors.add("42");
        actors.add("foo");

        // when
        when(movie.getId()).thenReturn(1);
        when(movieDao.findSameMovies(Mockito.any())).thenReturn(movieList);
        doNothing().when(movieDao).deleteOldConnections(Mockito.<Integer>any());
        doNothing().when(movieDao).updateMovie(Mockito.<Integer>any(), Mockito.any());
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.of(new Movie()));
        when(actor.getId()).thenThrow(new NotFoundException(""));
        Optional<Actor> ofResult = Optional.of(actor);
        when(actorDao.findActorByName(Mockito.any())).thenReturn(ofResult);

        // then
        assertThrows(NotFoundException.class,
                () -> movieService.updateMovie(1, new MovieRequest("Name", actors, LocalDate.of(1970, 1, 1))));
        verify(movieDao).findSameMovies(Mockito.any());
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(movieDao).deleteOldConnections(Mockito.<Integer>any());
        verify(movieDao).updateMovie(Mockito.<Integer>any(), Mockito.any());
        verify(movie).getId();
        verify(actorDao, atLeast(1)).findActorByName(Mockito.any());
        verify(actor).getId();
    }

    @Test
    void testDeleteMovie() {
        // when
        doNothing().when(movieDao).deleteMovie(Mockito.<Integer>any());
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.of(new Movie()));
        movieService.deleteMovie(1);

        // then
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(movieDao).deleteMovie(Mockito.<Integer>any());
    }

    @Test
    void testDeleteMovie1() {
        // when
        doThrow(new NotFoundException("")).when(movieDao).deleteMovie(Mockito.<Integer>any());
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.of(new Movie()));

        // then
        assertThrows(NotFoundException.class, () -> movieService.deleteMovie(1));
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
        verify(movieDao).deleteMovie(Mockito.<Integer>any());
    }

    @Test
    void testDeleteMovie2() {
        // when
        when(movieDao.selectMovieById(Mockito.<Integer>any())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> movieService.deleteMovie(1));
        verify(movieDao).selectMovieById(Mockito.<Integer>any());
    }
}

