package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.emus.UserStatus;
import com.gabriel.workshop_api.error.EmailDuplicateException;
import com.gabriel.workshop_api.error.UserNotFoundException;
import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class UserServiceTest {

    @Mock
    private final UserRepository userRepository;

    @InjectMocks
    private final UserService userService;


    @Test
    @DisplayName("Cadastrar usuário e salvar com sucesso")
    void loginUser() {

        User user = User.builder()
                .name("Gabriel")
                .email("gabrielDev@gmail.com")
                .password("Gabriel123@")
                .createdAt(LocalDateTime.now())
                .status(UserStatus.ACTIVED)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        User userSave = userService.save(user);

        assertEquals("Gabriel", userSave.getName());
        assertEquals("gabrielDev@gmail.com", userSave.getEmail());
    }

    @Test
    @DisplayName("Deve estourar uma exceção se o usuário não foi encontrado pelo id")
    void UserNotFound() {

        Long idUserNotExists = 999L;

        when(userRepository.findById(idUserNotExists)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(idUserNotExists);
        });
    }

    @DisplayName("validar se o usuário ja existe o mesmo email")
    @Test
    void testEmail() {

        User userEmail = User.builder()
                .id(1L)
                .name("Gabriel")
                .email("contatoGabriel12@gmail.com")
                .password("Gabriel@123")
                .build();

        when(userRepository.existsByEmail(userEmail.getEmail())).thenReturn(true);

        assertThrows(EmailDuplicateException.class, () -> {
            userService.save(userEmail);
        });
    }

    @DisplayName("Salvar usuários em conflitos")
    @Test
    void testConflictClient() {

        User user = User.builder()
                .id(1L)
                .name("Gabriel")
                .email("GabrielJose@gmail.com")
                .password("GabrielSantos120@")
                .status(UserStatus.ACTIVED)
                .build();

        User user1 = User.builder()
                .id(2L)
                .name("Carlos")
                .email("GabrielJose@gmail.com")
                .password("carlosJose123@")
                .status(UserStatus.ACTIVED)
                .build();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        assertThrows(EmailDuplicateException.class, () -> {
            userService.save(user);
        });

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.save(user);

        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(true);

        assertThrows(EmailDuplicateException.class, () -> {
            userService.save(user1);
        });
    }

    @Test
    @DisplayName("atualizar usuário")
    void updateUser() {

        User userCurrent = User.builder()
                .name("")
                .build();
    }
}
