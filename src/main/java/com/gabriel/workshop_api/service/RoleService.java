package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.exception.RoleNotFoundException;
import com.gabriel.workshop_api.model.Registration;
import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.model.User;
import com.gabriel.workshop_api.repository.RoleRepository;
import com.gabriel.workshop_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Role save(Role save) {
        return roleRepository.save(save);
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Role> findByRole(String nameRole) {
        return roleRepository.findByRole(nameRole);
    }

    @Transactional(readOnly = true)
    public List<User> findAllRoles(Long id) {
        return roleRepository.findAllRolesByUser(id);
    }

    @Transactional
    public Role update(Role role) {

        Role foundRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new RoleNotFoundException("Este perfil de acesso não existe"));

        foundRole.update(role);

        return roleRepository.save(foundRole);
    }

    @Transactional
    public void delete(Long id) {

        if(!roleRepository.existsById(id)) {
            throw new RoleNotFoundException("Este perfil de acesso não existe para ser excluído");
        }

        roleRepository.deleteById(id);
    }
}
