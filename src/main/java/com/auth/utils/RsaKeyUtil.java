package com.auth.utils;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaKeyUtil {

    public static PrivateKey loadPrivateKey(String classpathLocation) throws Exception {
        String key = readKeyFromClasspath(classpathLocation);

        key = key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        return KeyFactory.getInstance("RSA")
                .generatePrivate(
                        new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key))
                );
    }

    public static PublicKey loadPublicKey(String classpathLocation) throws Exception {
        String key = readKeyFromClasspath(classpathLocation);

        key = key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        return KeyFactory.getInstance("RSA")
                .generatePublic(
                        new X509EncodedKeySpec(Base64.getDecoder().decode(key))
                );
    }

    private static String readKeyFromClasspath(String path) throws Exception {
        InputStream is = RsaKeyUtil.class
                .getClassLoader()
                .getResourceAsStream(path);

        if (is == null) {
            throw new IllegalArgumentException("Key file not found in classpath: " + path);
        }

        return new String(is.readAllBytes());
    }
}
