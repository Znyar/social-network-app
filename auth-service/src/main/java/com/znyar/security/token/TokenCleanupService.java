package com.znyar.security.token;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void removeRevokedTokens() {
        List<Token> revokedTokens = tokenRepository.findAllByRevokedTrue();
        tokenRepository.deleteAll(revokedTokens);
    }

}
