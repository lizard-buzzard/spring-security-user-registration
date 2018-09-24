package com.lizard.buzzard.service;

import com.lizard.buzzard.persistence.dao.RoleRepository;
import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.Role;
import com.lizard.buzzard.persistence.model.RolesPrivilege;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.web.dto.UserWithAuthorityRights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Provide service for registering account
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserWithAuthorityRights> getUsersList() {

        Map<User, List<List<Role>>> userMap = userRepository.findAll(Sort.by("lastname").descending()).stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.mapping(
                                User::getRoles,
                                Collectors.toList())
                        )
                );
        List<UserWithAuthorityRights> usersList = userMap.entrySet().stream().map(e -> {
            // value processing
            List<List<Role>> v = e.getValue();
            Map<String, String> privilegesForRole = v.stream().flatMap(s -> s.stream()).collect(Collectors.groupingBy(
                    Role::getRolename,
                    Collectors.mapping(
                            r -> r.getPrivileges().stream().map(RolesPrivilege::getName).collect(Collectors.joining(", ")),
                            Collectors.joining(", ")
                    )
            ));
            String rolesWithPrivileges = privilegesForRole.entrySet().stream()
                    .map(pr -> pr.getKey() + " = [ " + pr.getValue() + " ]").collect(Collectors.joining(";\n "));

            // key processing
            User k = e.getKey();
            UserWithAuthorityRights userWithAuthorityRights = new UserWithAuthorityRights();
            userWithAuthorityRights.setEmail(k.getEmail());
            userWithAuthorityRights.setEnabled(k.getEnabled());
            userWithAuthorityRights.setFirstname(k.getFirstname());
            userWithAuthorityRights.setLastname(k.getLastname());
            userWithAuthorityRights.setRoleWithPrivileges(rolesWithPrivileges);

            return userWithAuthorityRights;
        }).collect(Collectors.toList());

        return usersList;
    }
}
