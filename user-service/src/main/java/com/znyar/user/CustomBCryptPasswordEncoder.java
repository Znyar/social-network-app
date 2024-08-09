package com.znyar.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2Y;
import static at.favre.lib.crypto.bcrypt.BCrypt.with;
import static at.favre.lib.crypto.bcrypt.LongPasswordStrategies.truncate;

@Component
@RequiredArgsConstructor
public class CustomBCryptPasswordEncoder {

    public String encode(String rawPassword) {
        return with(VERSION_2Y, truncate(VERSION_2Y)).hashToString(6, rawPassword.toCharArray());
    }

}
