package com.android.library.arithmetic;

/**
 * Created by KEVIN.DAI on 15/11/26.
 */
public class HmacUtils {

    private static final String MD5 = "HmacMD5";
    private static final String SHA1 = "HmacSHA1";
    private static final String SHA256 = "HmacSHA256";
    private static final String SHA384 = "HmacSHA384";
    private static final String SHA512 = "HmacSHA512";

    public static String MD5(String data, String key) {

        byte[] encodeData = new HmacEncoder(MD5).encode(data.getBytes(), key.getBytes());
        return new String(Hex.encode(encodeData));
    }

    public static String SHA1(String data, String key) {

        byte[] encodeData = new HmacEncoder(SHA1).encode(data.getBytes(), key.getBytes());
        return new String(Hex.encode(encodeData));
    }

    public static String SHA256(String data, String key) {

        byte[] encodeData = new HmacEncoder(SHA256).encode(data.getBytes(), key.getBytes());
        return new String(Hex.encode(encodeData));
    }

    public static String SHA384(String data, String key) {

        byte[] encodeData = new HmacEncoder(SHA384).encode(data.getBytes(), key.getBytes());
        return new String(Hex.encode(encodeData));
    }

    public static String SHA512(String data, String key) {

        byte[] encodeData = new HmacEncoder(SHA512).encode(data.getBytes(), key.getBytes());
        return new String(Hex.encode(encodeData));
    }
}