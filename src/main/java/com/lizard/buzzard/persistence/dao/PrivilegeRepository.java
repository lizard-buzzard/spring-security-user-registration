package com.lizard.buzzard.persistence.dao;

import com.lizard.buzzard.persistence.model.RolesPrivilege;

public interface PrivilegeRepository extends BaseRepository<RolesPrivilege, Long> {

    RolesPrivilege findByName(String privilege);
}
