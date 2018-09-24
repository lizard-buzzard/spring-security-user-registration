package com.lizard.buzzard.event;

import com.lizard.buzzard.persistence.dao.PrivilegeRepository;
import com.lizard.buzzard.persistence.dao.RoleRepository;
import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.Role;
import com.lizard.buzzard.persistence.model.RolesPrivilege;
import com.lizard.buzzard.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class OnContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("passwordEncoder")
    PasswordEncoder passwordEncoder;

    /**
     * Check if the DB (RolesPrivilege and Role entities) is populated with data and fill it in if not
     *
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // check and create privileges for a role
        RolesPrivilege userPagePriv = createPrivilege("USER_PAGE_PRIVILEGE");
        RolesPrivilege adminPagePriv = createPrivilege("ADMIN_PAGE_PRIVILEGE");
        RolesPrivilege readPriv = createPrivilege("READ_PRIVILEGE");
        RolesPrivilege writePriv = createPrivilege("WRITE_PRIVILEGE");
        RolesPrivilege changePasswordPriv = createPrivilege("CHANGE_PASSWORD_PRIVILEGE");

        // check and create users' roles
        Role roleUser = createRole("ROLE_USER", readPriv, changePasswordPriv, userPagePriv);
        Role roleAdmin = createRole("ROLE_ADMIN", readPriv, writePriv, changePasswordPriv, adminPagePriv);

        // create users with admin role
        createAdminUser("Sys", "Admin", "admin", "admin", roleAdmin);
        createAdminUser("Super", "User", "superuser", "super", roleAdmin, roleUser);
    }

    @Transactional
    private Role createRole(String name, RolesPrivilege... privileges) {
        Role rl = roleRepository.matchToPredicate(p -> p.getRolename().equals(name));
        return rl == null ?
                roleRepository.save(new Role(name, new ArrayList<RolesPrivilege>(Arrays.asList(privileges)))) : rl;
    }

    @Transactional
    private RolesPrivilege createPrivilege(String name) {
        RolesPrivilege rp = privilegeRepository.matchToPredicate(p -> p.getName().equals(name));
        return rp == null ? privilegeRepository.save(new RolesPrivilege(name)) : rp;
    }

    @Transactional
    private User createAdminUser(String firstName, String lastName, String email, String passwd, Role... roles) {
        return userRepository
                .findByFirstnameAndLastnameAndEmail(firstName, lastName, email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setEnabled(true);
                    user.setFirstname(firstName);
                    user.setLastname(lastName);
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode(passwd));
                    user.setRoles(Arrays.asList(roles));
                    return userRepository.save(user);
                });
    }
}
