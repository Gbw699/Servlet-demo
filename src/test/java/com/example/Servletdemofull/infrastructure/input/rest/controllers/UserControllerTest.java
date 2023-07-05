package com.example.Servletdemofull.infrastructure.input.rest.controllers;

import com.example.Servletdemofull.infrastructure.input.rest.mappers.UserMapper;
import com.example.Servletdemofull.infrastructure.output.entity.User;
import com.example.Servletdemofull.infrastructure.output.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
    }


    @Test
    void getAllUsers() throws Exception {
        //given
        List<User> users = new ArrayList<>(
                Arrays.asList(
                        new User(UUID.randomUUID(), "Juan", "Perez", "grg@gmail.com", "+542616320489"),
                        new User(UUID.randomUUID(), "Juan", "Perez", "grg@gmail.com", "+542616320489"),
                        new User(UUID.randomUUID(), "Juan", "Perez", "grg@gmail.com", "+542616320489")
                )
        );

        //expectation
        when(userRepository.findAll()).thenReturn(users);

        //performs
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getUserById() throws Exception {
        //given
        UUID id = UUID.fromString("9bc28702-826b-4d60-b4c5-015f24484d6d");
        User user = new User(id, "Juan", "Perez", "grg@gmail.com", "+542616320489");

        //expectation
        when(userRepository.findById(id)).thenReturn(Optional.of(user), Optional.empty());

        //performs
        mockMvc.perform(get("/api/user/{id}", id))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/api/user/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void createUser() throws Exception {
        //Given
        User user = new User(UUID.randomUUID(), "Juan", "Perez", "grg@gmail.com", "+542616320489");

        //expectation (se utliza cuando se espera algo de la BDD)
//        when(userRepository.save(user)).thenReturn(null);

        //perform
        mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateUser() throws Exception {
        //Given
        UUID id = UUID.randomUUID();
        User user = new User(id, "Juan", "Perez", "grg@gmail.com", "+542616320489");

        //expectation
        when(userRepository.findById(id)).thenReturn(Optional.of(user), Optional.empty());
//        when(userRepository.save(user)).thenReturn(null);

        //perform
        mockMvc.perform(put("/api/user/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(put("/api/user/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteAllUsers() throws Exception {
        //given
        List<User> users = new ArrayList<>();

        //expectation
//        when(userRepository.findAll()).thenReturn(users);
        doNothing().when(userRepository).deleteAll();

        //perform
        mockMvc.perform(delete("/api/user"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteUserById() throws Exception {
        //given
        UUID id = UUID.fromString("a0e0a6e5-5b76-4e39-8842-33120204d1d8");
        User user = new User(id, "Juan", "Perez", "grg@gmail.com", "+542616320489");

        //expectation
        when(userRepository.findById(id)).thenReturn(Optional.of(user), Optional.empty());
        //perform
        mockMvc.perform(delete("/api/user/{id}", id))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(delete("/api/user/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}