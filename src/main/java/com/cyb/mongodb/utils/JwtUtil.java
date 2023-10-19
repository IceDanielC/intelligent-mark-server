package com.cyb.mongodb.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Slf4j
//JWT工具类
public class JwtUtil {
    // TOKEN的有效期一天（S）
    private static final int TOKEN_TIME_OUT = 3600;
    // 加密KEY
    private static final String TOKEN_ENCRY_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";
    // 最小刷新间隔(S)
    private static final int REFRESH_TIME = 300;

    // 生产token
    public static String getToken(Long id) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("id", id);
        long currentTime = System.currentTimeMillis();
        return Jwts.builder().setHeaderParam("typ", "JWT").
                setSubject(id + "").
                setIssuedAt(new Date(currentTime)).
                setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000)).
                signWith(SignatureAlgorithm.HS512, generalKey()).
                compact();
    }

    /**
     * 获取token中的claims信息,包括header，payload和signature
     *
     * @param token
     * @return
     */
    private static Jws<Claims> getJws(String token) {
        return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token);
    }

    /**
     * 获取payload body信息
     *
     * @param token
     * @return
     */
    public static Claims getClaimsBody(String token) {
        try {
            return getJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    /**
     * 是否过期
     *
     * @param claims
     * @return 0：有效，1：过期，2：过期
     */
    public static int verifyToken(Claims claims) {
        if (claims == null) {
            return 1;
        }
        if (claims.getExpiration().before(new Date())) { //过期
            return 2;
        }
        //没有过期
        return 0;
    }

    /**
     * 由字符串生成加密key
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(TOKEN_ENCRY_KEY.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    // 获取fresh_token
    public static String getFreshToken(Long id) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("id", id);
        long currentTime = System.currentTimeMillis();
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(id + "").
                setIssuedAt(new Date(currentTime)).
                setExpiration(new Date(currentTime + 3600 * 24 * 3 * 1000)).
                signWith(SignatureAlgorithm.HS512, generalKey()).
                compact();
    }

    public static void main(String[] args) {
       /* Map map = new HashMap();
        map.put("id","11");*/
        String token = JwtUtil.getToken(1102L);
        System.out.println(token);
        //解密的时候验证ID
        Jws<Claims> jws = JwtUtil.getJws(token);//如果token不对或被篡改，则抛出异常
        System.out.println(jws);
        Claims claims = jws.getBody();
        System.out.println("sub: " + getClaimsBody(token).get("sub"));
        System.out.println("过期时间：" + JwtUtil.getClaimsBody(token).getExpiration());
    }
}