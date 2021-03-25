package com.example.reddit.security;

import com.example.reddit.exceptions.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

@Service

public class JwtProvider {

//
//    private String SECRET_KEY = "secret";
//
//    public String getUsernameFromJwt(String token) {
//        Claims claims = parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }

    /*
    this class can also be called JwtUtil
    密钥对：
      公钥(证书)和私钥成对存在。通信双方各持有自己的私钥和对方的公钥。自己的私钥需密切保护，而公钥是公开给对方的。
      在windows下，单独存在的公钥一般是后缀为.cer的文件
      A用自己的私钥对数据加密，发给B，B用A提供的公钥解密。同理B用自己的私钥对数据加密，发送给A后，A用B的公钥解开。
            公钥的两个用途：
            1。验证对方身份：防止其他人假冒对方发送数据给你。
            2。解密。
            私钥的两个用途：
            1。表明自己身份：除非第三方有你私钥，否则无法假冒你发送数据数据给对方。
            2。加密。

        jks(Java key store)：
        java用的存储密钥的容器。可以同时容纳n个公钥或私钥，后缀一般是.jks或者.keystore或.truststore等，千奇百怪。
        不管什么后缀，它就是一个容器，各个公司或机构叫法不同而已。比如把只包含"受信任的公钥"的容器存成.truststore文件等。
        用jdk\bin目录下的keytool.exe对其进行查看，导入，导出，删除，修改密码等各种操作。可以对jks容器加密码，输入正确才可以操作此容器中密钥。
        还有一个密码的概念与上者不同，是jks中存储着的私钥的密码，通常是绝密的。
        意思就是一个keystore(.jks)文件通常里面会装有公钥和私钥，keystore本身有一个密码，
        输入这个密码可以导出公钥为cer文件，而还有一个私钥是看不到的，想看私钥要输入另一个密码。意思是这样，不一定有x相应的命令对应


        *********** jwt的目的 *************
        最主要的目的：服务器应用在接受到JWT后，会首先对头部和载荷的内容用同一算法再次签名，如果服务器应用对头部和载荷再次以同样方法签名之后发现，
        自己计算出来的签名和接受到的签名不一样，那么就说明这个Token的内容被别人动过的，我们应该拒绝这个Token，
        返回一个HTTP 401 Unauthorized响应。

        Asymmetric Encryption uses two distinct, yet related keys.
        One key, the Public Key, is used for encryption and the other, the Private Key, is for decryption.
        As implied in the name, the Private Key is intended to be private
        so that only the authenticated recipient can decrypt the message.

        @PostConstruct注解的方法将会在依赖注入完成后被自动调用。


        getPrincipal() always return to one type: Object

        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        And the return object is type of org.springframework.security.core.userdetails.User.
        If you use predefined User class of Spring Security then you can cast it to User type:

        org.springframework.security.core.userdetails.User user = (User)object;
     */

//    public String generateToken(Authentication authentication){
//        User principal = (User) authentication.getPrincipal();
//        return Jwts.builder().setSubject(principal.getUsername()).signWith(SignatureAlgorithm.HS256,SECRET_KEY)
//                .setExpiration(Date.from(Instant.now().plusMillis(90000)))
//                .compact();
//
//    }
//
//    public String generateTokenWithUserName(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(from(Instant.now()))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .setExpiration(from(Instant.now().plusMillis(90000)))
//                .compact();
//    }
//
//
//    public boolean validateToken(String jwt) {
//        parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
//        return true;
//    }
//


    //The Java KeyStore is a database that can contain keys. A Java KeyStore is represented by the KeyStore (java.security.KeyStore) class. A KeyStore can be written to disk and read again. The KeyStore as a whole can be protected with a password, and each key entry in the KeyStore can be protected with its own password. This makes the KeyStore class a useful mechanism to handle encryption keys securely.
    //
    //A KeyStore can hold the following types of keys:
    //
    //Private keys
    //Public keys + certificates
    //Secret keys
    private KeyStore keyStore;
    private final int jwtExpirationInMillis = 90000;


    //@PostConstruct注解的方法将会在依赖注入完成后被自动调用
    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore", e);
        }

    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(SignatureAlgorithm.HS256,getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUserName(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(SignatureAlgorithm.HS256,getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception occured while retrieving public key from keystore", e);
        }
    }

    public boolean validateToken(String jwt) {
        parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occured while " +
                    "retrieving public key from keystore", e);
        }
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public int getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }


}
