package com.saimone.jdbctemplateapi.movie;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saimone.jdbctemplateapi.request.MovieRequest;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = MovieController.class)
@ExtendWith(SpringExtension.class)
class MovieControllerTest {
    @Autowired
    private MovieController movieController;

    @MockBean
    private MovieService movieService;

    @Test
    void testGetMovieById() throws Exception {
        // when
        when(movieService.getMovieById(Mockito.<Integer>any())).thenReturn(new Movie());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/movies/{id}", 1);
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();

        // then
        resultActions.andExpect(contentResult.string(String.join("", "{\"id\":null,\"name\":null,\"actors\":null,\"",
                System.getProperty("jdk.debug"), "Date\":null}")));
    }

    @Test
    void testGetMovieById1() throws Exception {
        // when
        when(movieService.selectAllMovies()).thenReturn(new ArrayList<>());
        when(movieService.getMovieById(Mockito.<Integer>any())).thenReturn(new Movie());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/movies/{id}", "",
                "Uri Variables");

        // then
        MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testUpdateMovie() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        doNothing().when(movieService).updateMovie(Mockito.<Integer>any(), Mockito.any());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/v1/movies/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new MovieRequest("Name", new ArrayList<>(), null)));

        // then
        MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateMovie1() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        doNothing().when(movieService).addNewMovie(Mockito.any());
        doNothing().when(movieService).updateMovie(Mockito.<Integer>any(), Mockito.any());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/api/v1/movies/{id}", "", "Uri Variables")
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new MovieRequest("Name", new ArrayList<>(), null)));

        // then
        MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteMovie() throws Exception {
        // when
        doNothing().when(movieService).deleteMovie(Mockito.<Integer>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/movies/{id}", 1);

        // then
        MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteMovie1() throws Exception {
        // when
        doNothing().when(movieService).deleteMovie(Mockito.<Integer>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/movies/{id}", 1);
        requestBuilder.characterEncoding("Encoding");

        // then
        MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAddNewMovie() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        when(movieService.selectAllMovies()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new MovieRequest("Name", new ArrayList<>(), null)));

        // then
        MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testAddNewMovie1() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie());

        // when
        when(movieService.selectAllMovies()).thenReturn(movieList);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new MovieRequest("Name", new ArrayList<>(), null)));

        // then
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(String.join("", "[{\"id\":null,\"name\":null,\"actors\":null,\"",
                System.getProperty("jdk.debug"), "Date\":null}]")));
    }

    @Test
    void testAllMoviesList() throws Exception {
        // when
        when(movieService.selectAllMovies()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/movies");

        // then
        MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testAllMoviesList1() throws Exception {
        // given
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie());

        // when
        when(movieService.selectAllMovies()).thenReturn(movieList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/movies");
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();

        // then
        resultActions.andExpect(contentResult.string(String.join("", "[{\"id\":null,\"name\":null,\"actors\":null,\"",
                System.getProperty("jdk.debug"), "Date\":null}]")));
    }

    @Test
    void testAllMoviesList2() throws Exception {
        // when
        when(movieService.selectAllMovies()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/movies");
        requestBuilder.characterEncoding("Encoding");

        // then
        MockMvcBuilders.standaloneSetup(movieController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

