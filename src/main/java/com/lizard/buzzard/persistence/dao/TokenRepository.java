package com.lizard.buzzard.persistence.dao;

import com.lizard.buzzard.persistence.model.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends BaseRepository<VerificationToken, Long> {
}
