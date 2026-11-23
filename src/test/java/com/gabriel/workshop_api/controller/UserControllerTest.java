package com.gabriel.workshop_api.controller;

import com.gabriel.workshop_api.config.security.JwtService;
import com.gabriel.workshop_api.config.security.Security;
import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.dto.request.UserRequest;
import com.gabriel.workshop_api.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(Security.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("cadastrar um usuário no sistema")
    void createUser() throws Exception {

      UserRequest userRequest = new UserRequest("Gabriel", "contatogabriel123@gmail.com", "gabriel@123");

      User user = userRequest.toEntityUser();

      user.setId(1L);
      user.setIdUser("USER-1233");

      when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.role").isArray());
    }

    @Test
    @DisplayName("verificando se a API buscar pelo id")
    void getUser() throws Exception {

       User userRequest = new UserRequest("Gabriel", "contatogabriel123@gmail.com", "gabriel123@").toEntityUser();

       userRequest.setId(1L);

       userRequest.setIdUser("USER-1233");

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("cadastrar com alguns campos inexistentes")
    void nullHolder() throws Exception {

        User user = new UserRequest("")

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.name").value(""))
                .andExpect(jsonPath("$.email").value(""))
                .andExpect(jsonPath("$.password").value(""))
                .andExpect(jsonPath("$.password").value(""));
    }
}
