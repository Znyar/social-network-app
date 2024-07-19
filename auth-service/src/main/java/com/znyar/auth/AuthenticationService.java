package com.znyar.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.znyar.exception.NoValidTokenFoundException;
import com.znyar.exception.UserNotFoundException;
import com.znyar.security.CustomBCryptPasswordEncoder;
import com.znyar.security.JwtService;
import com.znyar.security.token.Token;
import com.znyar.security.token.TokenRepository;
import com.znyar.security.token.TokenType;
import com.znyar.user.UserClient;
import com.znyar.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserClient userClient;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final CustomBCryptPasswordEncoder encoder;

    public void register(RegistrationRequest request) {
        request.setPassword(encoder.encode(request.getPassword()));
        userClient.saveUser(request);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userClient.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "Cannot find user with e-mail " + request.getEmail()));
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new SecurityException("Invalid credentials");
        }
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        tokenRepository.findTokenByUserIdAndExpiredIsFalseAndRevokedIsFalse(user.getId())
                .ifPresent(t -> {
                    t.setRevoked(true);
                    t.setExpired(true);
                    tokenRepository.save(t);
                });
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .userId(user.getId())
                .token(jwtToken)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = userClient.getUserByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException(
                            "Cannot find user with e-mail " + userEmail
                    ));
            if (jwtService.isTokenNonExpired(refreshToken)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public boolean validateToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String token;
        final String userEmail;
        token = authHeader.substring(7);
        userEmail = jwtService.extractUsername(token);
        if (userEmail!= null) {
            User user = userClient.getUserByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException(
                            "Cannot find user with e-mail " + userEmail
                    ));
            String storedValidToken = tokenRepository.findTokenByUserIdAndExpiredIsFalseAndRevokedIsFalse(user.getId())
                    .map(Token::getToken)
                    .orElseThrow(() -> new NoValidTokenFoundException("No valid token found"));
            return jwtService.isTokenValid(token, storedValidToken);
        }
        return false;
    }

    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
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
