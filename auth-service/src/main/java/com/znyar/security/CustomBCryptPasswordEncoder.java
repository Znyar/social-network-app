package com.znyar.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static at.favre.lib.crypto.bcrypt.BCrypt.*;
import static at.favre.lib.crypto.bcrypt.BCrypt.Version.*;
import static at.favre.lib.crypto.bcrypt.LongPasswordStrategies.*;

@Component
@RequiredArgsConstructor
public class CustomBCryptPasswordEncoder {

    public String encode(String password) {
        return with(VERSION_2Y, truncate(VERSION_2Y)).hashToString(6, password.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return verifyer(VERSION_2Y, truncate(VERSION_2Y)).verify(rawPassword.getBytes(), encodedPassword.getBytes()).verified;
    }

}
