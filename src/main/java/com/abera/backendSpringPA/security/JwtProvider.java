package com.abera.backendSpringPA.security;

import com.abera.backendSpringPA.exception.SpringPAException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init() {

        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springPA.jks");
            keyStore.load(resourceAsStream, "123456".toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new SpringPAException("Exception occured while loading keystore " + e);
        }

    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + (2 * 60 * 1000)))
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springPA", "123456".toCharArray());
        } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
            throw new SpringPAException("Exception occured while loading retrieving public key from keystore " + e);
        }
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("springPA").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringPAException("Exception occured while loading retrieving public key from keystore " + e);
        }
    }

    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPrivateKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
