package com.saimone.jdbctemplateapi.actor;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = ActorController.class)
@ExtendWith(SpringExtension.class)
class ActorControllerTest {
    @Autowired
    private ActorController actorController;

    @MockBean
    private ActorService actorService;

    @Test
    void testGetActorById() throws Exception {
        // when
        when(actorService.getActorById(Mockito.<Integer>any())).thenReturn(new Actor());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/actors/{id}", 1);

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":null,\"name\":null,\"movies\":null}"));
    }

    @Test
    void testGetActorById1() throws Exception {
        // when
        when(actorService.selectAllActors()).thenReturn(new ArrayList<>());
        when(actorService.getActorById(Mockito.<Integer>any())).thenReturn(new Actor());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/actors/{id}", "",
                "Uri Variables");

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testUpdateActor() throws Exception {
        // given
        Actor actor = new Actor();
        actor.setId(1);
        actor.setMovies(new ArrayList<>());
        actor.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(actor);

        // when
        doNothing().when(actorService).updateActor(Mockito.<Integer>any(), Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/actors/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateActor1() throws Exception {
        // given
        Actor actor = new Actor();
        actor.setId(1);
        actor.setMovies(new ArrayList<>());
        actor.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(actor);

        // when
        doNothing().when(actorService).addNewActor(Mockito.any());
        doNothing().when(actorService).updateActor(Mockito.<Integer>any(), Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/actors/{id}", "", "Uri Variables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAddNewActor() throws Exception {
        // given
        Actor actor = new Actor();
        actor.setId(1);
        actor.setMovies(new ArrayList<>());
        actor.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(actor);

        // when
        when(actorService.selectAllActors()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testDeleteActor() throws Exception {
        // when
        doNothing().when(actorService).deleteActor(Mockito.<Integer>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/actors/{id}", 1);

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteActor1() throws Exception {
        // when
        doNothing().when(actorService).deleteActor(Mockito.<Integer>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/actors/{id}", 1);
        requestBuilder.characterEncoding("Encoding");

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAllActorsList() throws Exception {
        // when
        when(actorService.selectAllActors()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/actors");

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testAllActorsList1() throws Exception {
        // when
        when(actorService.selectAllActors()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/actors");
        requestBuilder.characterEncoding("Encoding");

        // then
        MockMvcBuilders.standaloneSetup(actorController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}