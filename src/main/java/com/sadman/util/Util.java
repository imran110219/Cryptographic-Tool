package com.sadman.util;

import sun.misc.BASE64Decoder;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
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

    public static String getPemPublicKey(String filename, String algorithm) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        String temp = new String(keyBytes);
        String publicKeyPEM = temp.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("\n", "");

        return publicKeyPEM;
    }
}
