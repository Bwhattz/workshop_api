package com.gabriel.workshop_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.workshop_api.emus.UserStatus;
import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.gabriel.workshop_api.WorkShopApiApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserTestController {

    @Autowired
    private final MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper;

    public UserTestController(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("cadastrar um usuário no sistema")
    void logginUser() throws Exception {

        User user = User.builder()
                .name("Gabriel")
                .email("contatohatz@gmail.com")
                .password("gabriel@123")
                .status(UserStatus.ACTIVED)
                .createdAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Gabriel"))
                .andExpect(jsonPath("$.email").value("gabriel.lv.lucas@gmail.com"))
                .andExpect(jsonPath("$.password").value("gabriel@123"))
                .andExpect(jsonPath("$.status").value("ACTIVED"))
                .andExpect(jsonPath("$.created_at").exists());
    }

    @Test
    @DisplayName("verificando se a API buscar pelo id")
    void getUser() throws Exception {

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("cadastrar com alguns campos inexistentes")
    void nullHolder() throws Exception {


        User user = User.builder()
                .name("")
                .email("")
                .password("")
                .build();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.name").value(""))
                .andExpect(jsonPath("$.email").value(""))
                .andExpect(jsonPath("$.password").value(""))
                .andExpect(jsonPath("$.password").value(""));
    }
}
