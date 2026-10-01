package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.repository.RoleRepository;
import com.gabriel.workshop_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Role save(Role roleSave) {

        Role savedRole = roleRepository.save(roleSave);

        User foundUser = userRepository.findById(savedRole.getId())
                .orElseThrow(() -> new EntityNotFoundException("Este usuário não existe para esse perfil de acesso"));


        if(!foundUser.getRoles().contains(savedRole)) {
            foundUser.getRoles().add(savedRole);
        }

        return roleRepository.save(savedRole);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role update(Role role) {

        Role foundRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new EntityNotFoundException("Este perfil de acesso não existe"));

        foundRole.update(role);

        return roleRepository.save(foundRole);
    }

    @Transactional
    public void delete(Long id) {

        if(!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Este perfil de acesso não existe para ser excluído");
        }

        roleRepository.deleteById(id);
    }
}
