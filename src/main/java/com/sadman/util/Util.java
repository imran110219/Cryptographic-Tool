package com.sadman.util;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Base64;
import java.util.Properties;

/**
 * @author Sadman
 */
public class Util {
    public static String convertKeyToString(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static Key convertStringToKey(String keyString, String algorithm){
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);
        return key;
    }

    public static String readApplicationProperty(String name) throws IOException {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = loader.getResourceAsStream("application.properties")) {
            properties.load(is);
        }
        return properties.getProperty(name);
    }
}
