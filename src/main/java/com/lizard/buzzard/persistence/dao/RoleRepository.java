package com.lizard.buzzard.persistence.dao;

import com.lizard.buzzard.persistence.model.Role;

public interface RoleRepository extends BaseRepository<Role, Long> {
    Role findByRolename(String rolename);
}
