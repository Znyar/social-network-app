package com.znyar.security;

import com.znyar.exception.NoValidTokenFoundException;
import com.znyar.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.*;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        tokenRepository.findByToken(jwt)
                .ifPresentOrElse(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                    tokenRepository.save(token);
                }, () -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    throw new NoValidTokenFoundException("Token not found");
                });
    }

}
