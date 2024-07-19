package com.znyar.auth;

import com.znyar.exception.AuthServiceProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate;
    @Value("${application.config.auth-url}")
    private String authUrl;

    public boolean isTokenValid(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<String> requestEntity = new HttpEntity<>(token, headers);
        ParameterizedTypeReference<Boolean> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                authUrl + "/validate-token?token=" + token,
                GET,
                requestEntity,
                responseType
        );
        if (responseEntity.getStatusCode().isError()) {
            throw new AuthServiceProcessingException("An error occurred while validating token");
        }
        return Boolean.TRUE.equals(responseEntity.getBody());
    }

}

