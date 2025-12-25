package com.itzhang.management.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static String signKey = "weiloong Zhang";
    private static Long expire = 43200000L;

    //将 Base64 字符串转换为 SecretKey
    private static Key key;


    static {
        //将原始密钥补足到 32 位（HS256 要求密钥长度至少 256 位）
        byte[] keyBytes = Decoders.BASE64.decode(signKey);
        if (keyBytes.length < 32) {
            byte[] newKeyBytes = new byte[32];
            System.arraycopy(keyBytes, 0, newKeyBytes, 0, keyBytes.length);
            key = Keys.hmacShaKeyFor(newKeyBytes);
        } else {
            key = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    /**
     * @param claims
     * @return java.lang.String
     * @Description jwt令牌生成
     * @Author weiloong_zhang
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims) // 新版用 .claims() 替代 .addClaims()
                .signWith(key)  // 自动推断算法（根据密钥类型）
                .expiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    /**
     * @param jwt
     * @return io.jsonwebtoken.Claims
     * @Description jwt令牌解析
     * @Author weiloong_zhang
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload(); // 新版用 getPayload() 替代 getBody()
    }
}
