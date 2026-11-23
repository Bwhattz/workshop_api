package com.gabriel.workshop_api.config.security;

import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class UserService implements UserDetailsService {
    
    private static final int MAX_TRY = 3;

    private final Map<String, Integer> attempts = new HashMap<>();

    private final UserRepository userRepository;

    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUserEmail(username)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }

    private void maxTry(User userPasswordTry) {

        int attempt = attempts.getOrDefault(userPasswordTry.getPassword(), 3);

        if(attempt >= MAX_TRY) {

        } else {
            attempts.put(userPasswordTry.getPassword(), attempt + 1);
        }
    }
}
