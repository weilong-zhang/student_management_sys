package com.itzhang.management.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Component
public class RandomCodeUtil {
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * @param length
     * @return java.lang.String
     * @Description 生成 n 位数的随机验证码
     * @Author weiloong_zhang
     */
    public String getRandomVerCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }
}
