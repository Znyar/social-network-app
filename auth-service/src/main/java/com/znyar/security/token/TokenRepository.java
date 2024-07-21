package com.znyar.security.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTokenByUserIdAndExpiredIsFalseAndRevokedIsFalse(Long userId);

    Optional<Token> findByToken(String token);

    List<Token> findAllByRevokedIsTrue();

    void deleteByToken(String token);

}
