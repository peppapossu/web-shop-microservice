package com.ks.authservice.security.jwt;

import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.Base64;

@Configuration
public class JwkConfig {

    @Value("${jwt.rsa.private-key}")
    private Resource privateKeyResource;

    @Value("${jwt.rsa.public-key}")
    private Resource publicKeyResource;

    @Value("${jwt.rsa.key-id}")
    private String keyId;

    @Bean
    public JWKSet jwkSet() throws Exception {
        RSAPrivateKey privateKey = loadPrivateKey();
        RSAPublicKey publicKey = loadPublicKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(keyId)
                .build();
        return new JWKSet(rsaKey);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) throws Exception {

        return (selector, context) -> selector.select(jwkSet);
    }

    private RSAPrivateKey loadPrivateKey() throws Exception {
        String key = readKey(privateKeyResource);
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                 .replace("-----END PRIVATE KEY-----", "")
                 .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) factory.generatePrivate(keySpec);
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        String key = readKey(publicKeyResource);
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                 .replace("-----END PUBLIC KEY-----", "")
                 .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) factory.generatePublic(keySpec);
    }

    private String readKey(Resource resource) throws Exception {
        try (InputStream is = resource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}