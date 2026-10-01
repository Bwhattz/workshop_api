package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    /**
     * Injeção do repositório para a regra de negócio
     */

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Transactional
    public User save(User user) {


        return userRepository.save(user);
    }

    /**
     * Valida se o email ja foi cadastro, para não gera conflito
     */

    public void validateEmail(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Valida se o email que o usuário alterou for igual ao email do outro usuário
     * Exemplo: quero alterar meu email para lucas1442@gmail.com, mas se ja existe esse email em outro usuário o sistema deve barrar
     * Entrar no erro @IllegalArgumentException
     */

    @Transactional
    public User update(User user) {

        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        userRepository.findByUser(user.getName(), user.getEmail()).ifPresent(existsUser -> {
            if(!foundUser.getName().equals(user.getName()) && !userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Este usuário ja existe");
            }
        });

        user.update(user);

        return userRepository.save(foundUser);
    }

    @Transactional
    public Optional<User> findByUser(String name, String email) {
        return userRepository.findByUser(name, email);
    }

    @Transactional
    public List<User> findByRoles(String roleName) {
        return userRepository.findByRolesNative(roleName);
    }

    @Transactional
    public void delete(Integer id) {

        if(!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }
}
