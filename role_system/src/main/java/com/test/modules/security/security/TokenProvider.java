package com.test.modules.security.security;

import com.test.modules.security.config.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * description: token提供者类
 *
 * @author RenShiWei
 * Date: 2020/8/3 15:23
 * <p>
 * 'InitializingBean' 初始化bean执行相应的方法
 **/
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    /** application.yml中对jwt的配置 */
    private final SecurityProperties properties;

    /** 标识声明(claims) 声明是JWT的“主体”，包含JWT创建者希望提供给JWT收件人的信息。 */
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_SIGN_KEY = "id";

    /** 签名密钥 */
    private Key key;

    public TokenProvider ( SecurityProperties properties ) {
        this.properties = properties;
    }

    @Override
    public void afterPropertiesSet () throws Exception {
        this.key = getSecretKey();
    }

    /**
     * description: SecretKey 根据 SECRET 的编码方式解码后得到：
     * Base64 编码：SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
     * Base64URL 编码：SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretString));
     * 未编码：SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
     * <p>
     * 也可采用key使用的是HMAC-SHA-256加密算法创建密钥
     * Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
     *
     * @return SecretKey 签名密钥
     * @author RenShiWei
     * Date: 2020/8/3 16:51
     */
    private SecretKey getSecretKey () {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getBase64Secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * description: 根据Authentication的主体信息（一般为账号）生成token
     *
     * @param authentication Spring Security使用的进行安全访问控制用户信息安全对象
     * @return token
     * @author RenShiWei
     * Date: 2020/8/3 17:03
     */
    public String createToken ( Authentication authentication ) {
        /*
         getAuthorities 获取权限集合
         getAuthority 获取已被授予的权限
       */
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 根据设置的token过期时间，设置生成token的使用期限
        long now = (new Date()).getTime();
        Date validity = new Date(now + properties.getTokenValidityInSeconds());

        //生成token
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * description: 根据authentication和用户id生成token
     *
     * @param authentication 用户信息
     * @param id             用户id
     * @return token
     * @author RenShiWei
     * Date: 2020/8/3 17:03
     */
    public String createToken ( Authentication authentication, Long id ) {
        /*
         getAuthorities 获取权限集合
         getAuthority 获取已被授予的权限
       */
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 根据设置的token过期时间，设置生成token的使用期限
        long now = (new Date()).getTime();
        Date validity = new Date(now + properties.getTokenValidityInSeconds());

        //生成token
        return Jwts.builder()
                .setSubject(id.toString())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * description: 获取用户信息安全对象
     *
     * @param token token信息
     * @return 用户信息安全对象
     * @author RenShiWei
     * Date: 2020/8/3 17:15
     */
    Authentication getAuthentication ( String token ) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * description: 验证token
     *
     * @param authToken token信息
     * @return 是否验证成功
     * @author RenShiWei
     * Date: 2020/8/3 17:22
     */
    boolean validateToken ( String authToken ) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * description: 从请求中获取token
     *
     * @param request 请求
     * @return token
     * @author RenShiWei
     * Date: 2020/8/3 17:24
     */
    public String getToken ( HttpServletRequest request ) {
        final String requestHeader = request.getHeader(properties.getHeader());
        if (requestHeader != null && requestHeader.startsWith(properties.getTokenStartWith())) {
            //截取前7个字符串
            return requestHeader.substring(7);
        }
        return null;
    }


}

