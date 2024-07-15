package com.znyar;

import com.znyar.user.UserClient;
import com.znyar.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class DemoController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<String> demo(@AuthenticationPrincipal User user) {
        User foundUser = userClient.getUserByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));
        return ResponseEntity.ok("Hello, " + foundUser.getEmail());
    }

}
