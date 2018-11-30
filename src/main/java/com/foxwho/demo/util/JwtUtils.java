package com.foxwho.demo.util;

import com.foxwho.demo.consts.JwtConsts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * JWT
 */
public class JwtUtils {
    private static final String ISS = "fox";

    /**
     * 角色的key
     */
    private static final String ROLE_CLAIMS = "rol";

    /**
     * 过期时间是3600秒，既是1个小时
     */
    private static final long EXPIRATION = 3600L;

    /**
     * 选择了记住我之后的过期时间为7天
     */
    private static final long EXPIRATION_REMEMBER = 604800L;

    /**
     * 创建token
     *
     * @param username
     * @param role
     * @param isRememberMe
     * @return
     */
    public static String createToken(String username, List role, boolean isRememberMe) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, JwtConsts.ROLE_DEFAULT);
        // 设置过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //30天
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date time = calendar.getTime();

        return Jwts.builder()
                .setSubject(username)
                .setClaims(map)
                .setIssuer(ISS)
                .setIssuedAt(new Date())
                // 设置过期时间30天
                .setExpiration(time)
                //采用什么算法是可以自己选择的，不一定非要采用HS512
                .signWith(SignatureAlgorithm.HS512, JwtConsts.SIGNING_KEY)
                .compact();
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    /**
     * 获取用户角色
     *
     * @param token
     * @return
     */
    public static String getUserRole(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    /**
     * 是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConsts.SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
