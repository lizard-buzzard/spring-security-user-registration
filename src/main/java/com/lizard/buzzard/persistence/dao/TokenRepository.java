package com.lizard.buzzard.persistence.dao;

import com.lizard.buzzard.persistence.model.VerificationToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends BaseRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
