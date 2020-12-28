package com.sadman.util;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

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
}
