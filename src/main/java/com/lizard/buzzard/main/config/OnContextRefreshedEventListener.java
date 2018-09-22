package com.lizard.buzzard.main.config;

import com.lizard.buzzard.persistence.dao.PrivilegeRepository;
import com.lizard.buzzard.persistence.dao.RoleRepository;
import com.lizard.buzzard.persistence.model.Role;
import com.lizard.buzzard.persistence.model.RolesPrivilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
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

    /**
     * Check if the DB (RolesPrivilege and Role entities) is populated with data and fill it in if not
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // check and create privileges for a role
        RolesPrivilege readPrivilege = createPrivilege("READ_PRIVILEGE");
        RolesPrivilege writePrivilege = createPrivilege("WRITE_PRIVILEGE");
        RolesPrivilege changePasswordPrivilege = createPrivilege("CHANGE_PASSWORD_PRIVILEGE");

        // check and create users' roles
        Role user = createRole("USER", readPrivilege, changePasswordPrivilege);
        Role admin = createRole("ADMIN", writePrivilege, changePasswordPrivilege);
    }

    @Transactional
    private Role createRole(String name, RolesPrivilege ... privileges) {
        Role rl = roleRepository.matchToPredicate(p->p.getRolename().equals(name));
        return rl == null ?
                roleRepository.save(new Role(name, new ArrayList<RolesPrivilege>(Arrays.asList(privileges)))) : rl;
    }

    @Transactional
    private RolesPrivilege createPrivilege(String name) {
        RolesPrivilege rp = privilegeRepository.matchToPredicate(p -> p.getName().equals(name));
        return rp == null ? privilegeRepository.save(new RolesPrivilege(name)) : rp;
    }

}
