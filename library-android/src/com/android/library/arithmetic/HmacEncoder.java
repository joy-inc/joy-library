package com.android.library.arithmetic;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by KEVIN.DAI on 15/11/26.
 */
public class HmacEncoder {

    private final String algorithm;

    public HmacEncoder(String algorithm) {

        this.algorithm = algorithm;
    }

    public byte[] getKey() {

        KeyGenerator keyGenerator;
        try {

            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e.getMessage());
        }
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    private static Key toKey(byte[] key, String algorithm) {

        return new SecretKeySpec(key, algorithm);
    }

    public byte[] encode(byte[] data, Key key) {

        Mac mac;
        try {

            mac = Mac.getInstance(algorithm);
            mac.init(key);
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return new byte[0];
        } catch (InvalidKeyException e) {

            e.printStackTrace();
            return new byte[0];
        }
        return mac.doFinal(data);
    }

    public byte[] encode(byte[] data, byte[] key) {

        return encode(data, toKey(key, algorithm));
    }
}