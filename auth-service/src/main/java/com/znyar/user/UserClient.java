package com.znyar.user;

import com.znyar.auth.RegistrationRequest;
import com.znyar.exception.UserServiceProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.*;

@Service
@RequiredArgsConstructor
public class UserClient {

    @Value("${application.config.user-url}")
    private String userUrl;
    private final RestTemplate restTemplate;

    public Optional<User> getUserByEmail(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<String> requestEntity = new HttpEntity<>(email, headers);
        ParameterizedTypeReference<User> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<User> responseEntity = restTemplate.exchange(
                userUrl + "/by-email?email=" + email,
                GET,
                requestEntity,
                responseType
        );
        if (responseEntity.getStatusCode().isError()) {
            throw new UserServiceProcessingException("An error occurred while finding the user: " + responseEntity.getStatusCode());
        }
        return Optional.ofNullable(responseEntity.getBody());
    }

    public void saveUser(RegistrationRequest registrationRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<RegistrationRequest> requestEntity = new HttpEntity<>(registrationRequest, headers);
        ParameterizedTypeReference<User> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<User> responseEntity = restTemplate.exchange(
                userUrl,
                POST,
                requestEntity,
                responseType
        );
        if (responseEntity.getStatusCode().isError()) {
            throw new UserServiceProcessingException("An error occurred while creating the user: " + responseEntity.getStatusCode());
        }
    }

}
