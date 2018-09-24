package com.lizard.buzzard.service;


import com.lizard.buzzard.web.dto.UserWithAuthorityRights;

import java.util.List;

/**
 * Provide service for registering account
 */
public interface AdminService {
    List<UserWithAuthorityRights> getUsersList();
}
