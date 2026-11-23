package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.emus.StatusProfile;
import com.gabriel.workshop_api.emus.UserStatus;
import com.gabriel.workshop_api.error.EmailDuplicateException;
import com.gabriel.workshop_api.error.RoleNotFoundException;
import com.gabriel.workshop_api.error.UserNotFoundException;
import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.repository.RoleRepository;
import com.gabriel.workshop_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * Injeção do repositório para a regra de negócio
     */

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Transactional
    public User save(User user) {

        String role = user.getRoles().stream()
                .filter(rol -> rol.getStatus() == StatusProfile.PARTICIPANT)
                .findFirst()
                .map(rol -> rol.getStatus().name())
                .orElseThrow(() -> new IllegalArgumentException("O usuário não está associado a nenhum cargo"));

        Role defaultRole = roleRepository.findByRole(role)
                        .orElseThrow(() -> new RoleNotFoundException("É necessário requer um cargo"));

        user.setEmail(user.getEmail().toLowerCase().trim());

        String validateEmail = validateEmail(user);

        user.setEmail(validateEmail);

        String passwordHash = passwordEncoder.encode(user.getPassword());

        user.setPassword(passwordHash);

        validatePassword(user);

        user.getRoles().clear();
        user.getRoles().add(defaultRole);

        return userRepository.save(user);
    }

    /**
     * Valida se o email ja foi cadastro, para não gera conflito
     */

    public String validateEmail(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new EmailDuplicateException("Email já cadastrado");
        }

        return user.getEmail().toLowerCase();
    }

    public void validatePassword(User user) {

        Objects.requireNonNull(user.getPassword(), "A senha não pode ser vazia");

        if(user.getPassword().length() < 6) {
            throw new IllegalArgumentException("A senha precisa conter pelo menos 8 caracteres");
        }
    }

    @Transactional(readOnly = true)
    @Cacheable("users")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable("users")
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Valida se o email que o usuário alterou for igual ao email do outro usuário
     * Exemplo: quero alterar meu email para lucas1442@gmail.com, mas se ja existe esse email em outro usuário o sistema deve barrar
     * Entrar no erro @IllegalArgumentException
     */

    @Transactional
    public User update(User user) {

        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        userRepository.findByUser(user.getName(), user.getEmail()).ifPresent(existsUser -> {
            if(!existsUser.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Já existe outro usuário cadastro com este nome e email");
            }
        });

        user.update(user);

        return userRepository.save(foundUser);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUser(String name, String email) {
        return userRepository.findByUser(name, email);
    }

    @Transactional(readOnly = true)
    public List<User> findByRoles(String roleName) {
        return userRepository.findByRolesNative(roleName);
    }

    @Transactional
    public void delete(Long id) {

        User foundUserToDelete = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        foundUserToDelete.setStatus(UserStatus.DESACTIVED);

        userRepository.deleteById(id);
    }
}
