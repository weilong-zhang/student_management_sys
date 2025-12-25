package com.itzhang.management.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CryptoUtil {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * @param rawPassword
     * @return java.lang.String
     * @Description 加密密码
     * @Author weiloong_zhang
     */
    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * @param rawPassword encryptedPassword
     * @return boolean
     * @Description 校验密码
     * @Author weiloong_zhang
     */
    public boolean matches(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }

}
