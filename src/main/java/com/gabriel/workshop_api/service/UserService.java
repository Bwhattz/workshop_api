package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.emus.RoleType;
import com.gabriel.workshop_api.exception.RoleNotFoundException;
import com.gabriel.workshop_api.exception.UserNotFoundException;
import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.repository.RoleRepository;
import com.gabriel.workshop_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        Role defaultRole = roleRepository.findById(user.getId())
                        .orElseThrow(() -> new RoleNotFoundException("Perfil de acesso não cadastrado no sistema"));

        validateEmail(user);

        String passwordCrypto = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordCrypto);

        user.getRoles().add(defaultRole);

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
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        userRepository.findByUser(user.getName(), user.getEmail()).ifPresent(existsUser -> {
            if(!foundUser.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Já existe outro usuário cadastro com este nome e email");
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
    public void delete(Long id) {

        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }
}
