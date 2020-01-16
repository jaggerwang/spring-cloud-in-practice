package net.jaggerwang.scip.common.adapter.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public String encode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public Boolean matches(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
